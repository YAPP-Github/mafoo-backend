package kr.mafoo.photo.service;

import static kr.mafoo.photo.domain.enums.PermissionLevel.FULL_ACCESS;
import static kr.mafoo.photo.domain.enums.PermissionLevel.VIEW_ACCESS;
import static kr.mafoo.photo.domain.enums.ShareStatus.ACCEPTED;

import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.enums.AlbumType;
import kr.mafoo.photo.domain.enums.BrandType;
import kr.mafoo.photo.exception.AlbumNotFoundException;
import kr.mafoo.photo.exception.AlbumOwnerChangeDeniedException;
import kr.mafoo.photo.exception.SharedMemberNotFoundException;
import kr.mafoo.photo.repository.AlbumRepository;
import kr.mafoo.photo.repository.PhotoRepository;
import kr.mafoo.photo.repository.SumoneEventMappingRepository;
import kr.mafoo.photo.service.dto.AlbumDto;
import kr.mafoo.photo.service.dto.SharedAlbumDto;
import kr.mafoo.photo.service.dto.SharedMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AlbumService {

    private final AlbumQuery albumQuery;
    private final AlbumCommand albumCommand;
    private final AlbumRepository albumRepository;

    private final PhotoCommand photoCommand;
    private final PhotoRepository photoRepository;


    private final AlbumPermissionVerifier albumPermissionVerifier;

    private final SharedMemberQuery sharedMemberQuery;
    private final MemberService memberService;
    private final SharedMemberCommand sharedMemberCommand;

    private final SumoneEventMappingRepository sumoneEventMappingRepository;

    @Transactional(readOnly = true)
    public Flux<AlbumDto> findAlbumListByMemberId(String memberId, String token) {
        return Flux.merge(
                findOwnedAlbumListByMemberId(memberId),
                findSharedAlbumListByMemberId(memberId, token)
        ).sort(Comparator.comparing(AlbumDto::createdAt).reversed());
    }

    private Flux<AlbumDto> findOwnedAlbumListByMemberId(String memberId) {
        return albumQuery.findByMemberId(memberId)
                .onErrorResume(AlbumNotFoundException.class, ex -> Mono.empty())
                .map(AlbumDto::fromOwnedAlbum);
    }

    private Flux<AlbumDto> findSharedAlbumListByMemberId(String memberId, String token) {
        return sharedMemberQuery.findAllByMemberIdWhereStatusNotRejected(memberId)
                .onErrorResume(SharedMemberNotFoundException.class, ex -> Mono.empty())
                .concatMap(sharedMember -> albumQuery.findById(sharedMember.getAlbumId())
                        .flatMap(album -> memberService.getMemberInfoById(album.getOwnerMemberId(), token)
                                .flatMap(member -> Mono.just(AlbumDto.fromSharedAlbum(album, sharedMember, member)))
                        )
                );
    }

    @Transactional(readOnly = true)
    public Mono<SharedAlbumDto> findAlbumDetailById(String albumId, String requestMemberId, String token) {
        return albumPermissionVerifier.verifyOwnershipOrAccessPermission(albumId, requestMemberId, VIEW_ACCESS)
                .flatMap(album -> sharedMemberQuery.findAllByAlbumIdWhereStatusNotRejected(albumId)
                        .onErrorResume(SharedMemberNotFoundException.class, ex -> Mono.empty())

                        .flatMap(sharedMember -> memberService.getMemberInfoById(sharedMember.getMemberId(), token)
                                .map(memberInfo -> SharedMemberDto.fromSharedMember(sharedMember, memberInfo)))
                        .sort(Comparator.comparing(SharedMemberDto::shareStatus))
                        .collectList()
                        .flatMap(sharedMembers -> memberService.getMemberInfoById(album.getOwnerMemberId(), token)
                                .map(ownerMember -> SharedAlbumDto.fromSharedAlbum(album, ownerMember, sharedMembers))
                        )

                        .switchIfEmpty(Mono.just(SharedAlbumDto.fromOwnedAlbum(album)))
                );
    }

    @Transactional
    public Mono<AlbumEntity> addAlbum(String albumName, String albumType, String requestMemberId) {
        return albumCommand.addAlbum(albumName, albumType, requestMemberId, null);
    }

    @Transactional
    public Mono<AlbumEntity> addSumoneAlbum(String albumName, String albumType, String requestMemberId, String inviteCode) {
        AtomicInteger displayIndex = new AtomicInteger(0);
        return albumCommand
                .addAlbum(albumName, albumType, requestMemberId, null)
                .flatMap(album -> sumoneEventMappingRepository
                        .findByInviteCode(inviteCode)
                        .switchIfEmpty(Mono.error(new AlbumNotFoundException()))
                        .flatMap(sumoneEventMappingEntity ->
                                sumoneEventMappingRepository.delete(sumoneEventMappingEntity).then(Mono.just(sumoneEventMappingEntity)))
                        .map(entity -> "SUMONE_" + entity.getId())
                        .flatMapMany(albumRepository::findAllByExternalId)
                        .flatMap(sumoneAlbum ->
                                photoRepository.findAllByAlbumIdOrderByCreatedAtAsc(sumoneAlbum.getAlbumId()).flatMap(photo ->
                                        photoCommand.addPhoto(
                                                photo.getPhotoUrl(),
                                                BrandType.EXTERNAL,
                                                album.getAlbumId(),
                                                displayIndex.getAndIncrement(),
                                                requestMemberId
                                        )
                                )
                        )
                        .then(Mono.just(album)))
                        .flatMap(album -> albumCommand.increaseAlbumPhotoCount(album, displayIndex.get()));
    }

    @Transactional
    public Mono<AlbumEntity> modifyAlbumNameAndType(String albumId, String newAlbumName, String newAlbumType, String requestMemberId) {
        return albumPermissionVerifier.verifyOwnershipOrAccessPermission(albumId, requestMemberId, FULL_ACCESS)
                .flatMap(album -> albumCommand.modifyAlbumNameAndType(album, newAlbumName, newAlbumType));
    }

    @Transactional
    public Mono<AlbumEntity> modifyAlbumOwnership(String albumId, String newOwnerMemberId, String requestMemberId) {
        return albumPermissionVerifier.verifyOwnership(albumId, requestMemberId)
                .flatMap(album -> sharedMemberQuery.findByAlbumIdAndMemberIdWhereStatusAccepted(albumId, newOwnerMemberId)
                        .onErrorResume(SharedMemberNotFoundException.class, ex ->
                                Mono.error(new AlbumOwnerChangeDeniedException())
                        )
                        .flatMap(sharedMemberCommand::removeSharedMember)
                        .then(albumCommand.modifyAlbumOwnership(album, newOwnerMemberId))
                        .then(sharedMemberCommand.addSharedMember(albumId, String.valueOf(FULL_ACCESS), Optional.of(ACCEPTED), requestMemberId))
                        .thenReturn(album)
                );
    }

    @Transactional
    public Mono<Void> removeAlbum(String albumId, String requestMemberId) {
        return albumPermissionVerifier.verifyOwnership(albumId, requestMemberId)
                .flatMap(albumCommand::removeAlbum);
    }

    public Mono<Long> countAlbumByAlbumType(AlbumType albumType) {
        return albumQuery.countAlbumByAlbumType(albumType);
    }
}

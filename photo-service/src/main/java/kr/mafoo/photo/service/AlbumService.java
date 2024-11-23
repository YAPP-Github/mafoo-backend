package kr.mafoo.photo.service;

import static kr.mafoo.photo.domain.enums.PermissionLevel.FULL_ACCESS;
import static kr.mafoo.photo.domain.enums.PermissionLevel.VIEW_ACCESS;

import java.util.Comparator;
import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.exception.AlbumNotFoundException;
import kr.mafoo.photo.exception.SharedMemberNotFoundException;
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

    private final AlbumPermissionVerifier albumPermissionVerifier;

    private final SharedMemberQuery sharedMemberQuery;
    private final MemberService memberService;

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

    private Flux<SharedMemberDto> findSharedAlbumMemberDetail(String albumId, String token) {
        return sharedMemberQuery.findAllByAlbumIdWhereStatusNotRejected(albumId)
            .concatMap(sharedMember -> memberService.getMemberInfoById(sharedMember.getMemberId(), token)
                .flatMap(memberInfo -> Mono.just(SharedMemberDto.fromSharedMember(sharedMember, memberInfo)))
            ).sort(Comparator.comparing(SharedMemberDto::shareStatus));
    }

    @Transactional
    public Mono<AlbumEntity> addAlbum(String albumName, String albumType, String requestMemberId) {
        return albumCommand.addAlbum(albumName, albumType, requestMemberId);
    }

    @Transactional
    public Mono<AlbumEntity> modifyAlbumNameAndType(String albumId, String newAlbumName, String newAlbumType, String requestMemberId) {
        return albumPermissionVerifier.verifyOwnershipOrAccessPermission(albumId, requestMemberId, FULL_ACCESS)
            .flatMap(album -> albumCommand.modifyAlbumNameAndType(album, newAlbumName, newAlbumType));
    }

    @Transactional
    public Mono<AlbumEntity> modifyAlbumOwnership(String albumId, String newOwnerMemberId, String requestMemberId) {
        return albumPermissionVerifier.verifyOwnership(albumId, requestMemberId)
            .flatMap(album -> albumCommand.modifyAlbumOwnership(album, newOwnerMemberId)
                // TODO : 앨범 내부 사진 소유자를 새로운 앨범 소유자로 변경
            );
    }

    @Transactional
    public Mono<Void> removeAlbum(String albumId, String requestMemberId) {
        return albumPermissionVerifier.verifyOwnership(albumId, requestMemberId)
            .flatMap(albumCommand::removeAlbum);
    }

}
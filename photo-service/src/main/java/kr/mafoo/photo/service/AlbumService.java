package kr.mafoo.photo.service;

import static kr.mafoo.photo.domain.enums.AlbumSortType.PHOTO_COUNT;
import static kr.mafoo.photo.domain.enums.PermissionLevel.FULL_ACCESS;
import static kr.mafoo.photo.domain.enums.PermissionLevel.VIEW_ACCESS;
import static kr.mafoo.photo.domain.enums.ShareStatus.ACCEPTED;
import static kr.mafoo.photo.domain.enums.SortOrder.DESC;

import java.util.Comparator;
import java.util.Optional;

import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.enums.AlbumSortType;
import kr.mafoo.photo.domain.enums.AlbumType;
import kr.mafoo.photo.domain.enums.BrandType;
import kr.mafoo.photo.domain.enums.SortOrder;
import kr.mafoo.photo.exception.AlbumNotFoundException;
import kr.mafoo.photo.exception.AlbumOwnerChangeDeniedException;
import kr.mafoo.photo.exception.SharedMemberNotFoundException;
import kr.mafoo.photo.repository.AlbumRepository;
import kr.mafoo.photo.repository.PhotoRepository;
import kr.mafoo.photo.repository.SumoneEventMappingRepository;
import kr.mafoo.photo.service.dto.ViewableAlbumDto;
import kr.mafoo.photo.service.dto.ViewableAlbumDetailDto;
import kr.mafoo.photo.service.dto.SharedMemberForAlbumDto;
import kr.mafoo.photo.exception.AlbumNotFoundException;
import kr.mafoo.photo.exception.AlbumOwnerChangeDeniedException;
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

    private final PhotoCommand photoCommand;

    private final SharedMemberQuery sharedMemberQuery;
    private final SharedMemberCommand sharedMemberCommand;

    private final MemberService memberService;

    @Transactional(readOnly = true)
    public Flux<ViewableAlbumDto> findViewableAlbumListByMemberId(String memberId) {
        return Flux.merge(
                findOwnedAlbumListByMemberId(memberId),
                findSharedAlbumListByMemberId(memberId)
            )
            .collectSortedList(Comparator.comparing(ViewableAlbumDto::createdAt).reversed())
            .flatMapIterable(albumList -> albumList);
    }

    @Transactional(readOnly = true)
    public Mono<ViewableAlbumDto> findViewableAlbumVariables(AlbumType type, AlbumSortType sort, SortOrder order, String memberId) {
        Comparator<ViewableAlbumDto> comparator = getComparator(sort, order);

        return Flux.merge(
                findOwnedAlbumListByMemberId(memberId),
                findSharedAlbumListByMemberId(memberId)
            )
            .filter(album -> isTypeMatching(album, type))
            .collectSortedList(comparator)
            .flatMapIterable(albumList -> albumList)
            .next();
    }

    private boolean isTypeMatching(ViewableAlbumDto album, AlbumType type) {
        return type == null || album.type().equals(type);
    }

    private Comparator<ViewableAlbumDto> getComparator(AlbumSortType sort, SortOrder order) {
        Comparator<ViewableAlbumDto> comparator;

        if (PHOTO_COUNT.equals(sort)) {
            comparator = Comparator.comparing(ViewableAlbumDto::photoCount);
        } else {
            comparator = Comparator.comparing(ViewableAlbumDto::createdAt);
        }

        if (DESC.equals(order)) {
            return comparator.reversed();
        } else {
            return comparator;
        }
    }

    private Flux<ViewableAlbumDto> findOwnedAlbumListByMemberId(String memberId) {
        return albumQuery.findByMemberId(memberId)
                .onErrorResume(AlbumNotFoundException.class, ex -> Mono.empty())
                .flatMap(album -> memberService.getMemberInfoById(album.getOwnerMemberId())
                    .flatMap(member -> Mono.just(ViewableAlbumDto.fromOwnedAlbum(album, member)))
                );
    }

    private Flux<ViewableAlbumDto> findSharedAlbumListByMemberId(String memberId) {
        return sharedMemberQuery.findAllByMemberIdWhereStatusNotRejected(memberId)
                .onErrorResume(SharedMemberNotFoundException.class, ex -> Mono.empty())
                .concatMap(sharedMember -> albumQuery.findById(sharedMember.getAlbumId())
                        .flatMap(album -> memberService.getMemberInfoById(album.getOwnerMemberId())
                                .flatMap(member -> Mono.just(
                                    ViewableAlbumDto.fromSharedAlbum(album, member, sharedMember)))
                        )
                );
    }

    @Transactional(readOnly = true)
    public Mono<ViewableAlbumDetailDto> findViewableAlbumDetailById(String albumId, String requestMemberId) {
        return albumPermissionVerifier.verifyOwnershipOrAccessPermission(albumId, requestMemberId, VIEW_ACCESS)
                .flatMap(album -> sharedMemberQuery.findAllByAlbumIdWhereStatusNotRejected(albumId)
                        .onErrorResume(SharedMemberNotFoundException.class, ex -> Mono.empty())

                        .flatMap(sharedMember -> memberService.getMemberInfoById(sharedMember.getMemberId())
                                .map(memberInfo -> SharedMemberForAlbumDto.fromSharedMember(sharedMember, memberInfo)))
                        .sort(Comparator.comparing(SharedMemberForAlbumDto::shareStatus))
                        .collectList()
                        .flatMap(sharedMembers -> memberService.getMemberInfoById(album.getOwnerMemberId())
                                .map(ownerMember -> ViewableAlbumDetailDto.fromSharedAlbum(album, ownerMember, sharedMembers))
                        )

                        .switchIfEmpty(Mono.just(ViewableAlbumDetailDto.fromOwnedAlbum(album)))
                );
    }

    @Transactional
    public Mono<AlbumEntity> addAlbum(String albumName, String albumType, String requestMemberId) {
        return albumCommand.addAlbum(albumName, albumType, requestMemberId, null);
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
                .flatMap(album -> sharedMemberCommand.removeShareMemberByAlbumId(albumId)
                    .thenMany(photoCommand.removePhotoByAlbumId(albumId))
                    .then(albumCommand.removeAlbum(albumId))
                );
    }

    public Mono<Long> countAlbumByAlbumType(AlbumType albumType) {
        return albumQuery.countAlbumByAlbumType(albumType);
    }
}

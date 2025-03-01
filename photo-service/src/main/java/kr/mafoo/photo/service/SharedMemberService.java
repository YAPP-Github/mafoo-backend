package kr.mafoo.photo.service;

import static kr.mafoo.photo.domain.enums.NotificationType.SHARED_MEMBER_INVITATION_ACCEPTED;
import static kr.mafoo.photo.domain.enums.NotificationType.SHARED_MEMBER_INVITATION_CREATED;
import static kr.mafoo.photo.domain.enums.PermissionLevel.FULL_ACCESS;
import static kr.mafoo.photo.domain.enums.ShareStatus.ACCEPTED;
import static kr.mafoo.photo.domain.enums.ShareStatus.PENDING;
import static kr.mafoo.photo.domain.enums.ShareStatus.REJECTED;
import static kr.mafoo.photo.domain.enums.SortOrder.DESC;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.SharedMemberEntity;
import kr.mafoo.photo.domain.enums.PermissionLevel;
import kr.mafoo.photo.domain.enums.ShareStatus;
import kr.mafoo.photo.domain.enums.SharedMemberSortType;
import kr.mafoo.photo.domain.enums.SortOrder;
import kr.mafoo.photo.exception.AlbumNotFoundException;
import kr.mafoo.photo.exception.SharedMemberNotFoundException;
import kr.mafoo.photo.exception.SharedMemberPermissionDeniedException;
import kr.mafoo.photo.service.dto.SharedMemberDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class SharedMemberService {

    private final SharedMemberQuery sharedMemberQuery;
    private final SharedMemberCommand sharedMemberCommand;

    private final AlbumPermissionVerifier albumPermissionVerifier;

    private final MemberServiceClient memberServiceClient;
    private final AlbumQuery albumQuery;

    @Transactional(readOnly = true)
    public Mono<SharedMemberDetailDto> findSharedMemberVariablesInOwnedAlbum(
        ShareStatus shareStatus, PermissionLevel permissionLevel, SharedMemberSortType sort, SortOrder order, String memberId) {

        return albumQuery.findByMemberId(memberId)
            .onErrorResume(AlbumNotFoundException.class, ex -> Mono.empty())
            .collectList()
            .filter(albumList -> !albumList.isEmpty())
            .flatMap(albumList -> {
                List<String> albumIdList = albumList.stream().map(AlbumEntity::getAlbumId).toList();
                return sharedMemberQuery.findAllByAlbumIdList(albumIdList)
                    .onErrorResume(SharedMemberNotFoundException.class, ex -> Flux.empty())
                    .collectList()
                    .flatMap(sharedMembers -> findSharedMemberVariables(sharedMembers, shareStatus, permissionLevel, sort, order));
            });
    }

    @Transactional(readOnly = true)
    public Mono<SharedMemberDetailDto> findSharedMemberVariablesInSharedAlbum(
        ShareStatus shareStatus, PermissionLevel permissionLevel, SharedMemberSortType sort, SortOrder order, String memberId) {

        return sharedMemberQuery.findByMemberId(memberId)
            .onErrorResume(SharedMemberNotFoundException.class, ex -> Mono.empty())
            .collectList()
            .filter(sharedMemberList -> !sharedMemberList.isEmpty())
            .flatMap(sharedMemberList -> {
                List<String> albumIdList = sharedMemberList.stream().map(SharedMemberEntity::getAlbumId).toList();
                return sharedMemberQuery.findAllByAlbumIdListAndMemberIdNot(albumIdList, memberId)
                    .onErrorResume(SharedMemberNotFoundException.class, ex -> Flux.empty())
                    .collectList()
                    .flatMap(sharedMembers -> findSharedMemberVariables(sharedMembers, shareStatus, permissionLevel, sort, order));
            });
    }

    private Mono<SharedMemberDetailDto> findSharedMemberVariables(List<SharedMemberEntity> sharedMemberEntities, ShareStatus shareStatus, PermissionLevel permissionLevel, SharedMemberSortType sort, SortOrder order) {

        Comparator<SharedMemberEntity> comparator = getComparator(sort, order);

        return Flux.fromIterable(sharedMemberEntities)
            .filter(sharedMember -> isShareStatusMatching(sharedMember, shareStatus))
            .filter(sharedMember -> isPermissionLevelMatching(sharedMember, permissionLevel))
            .collectSortedList(comparator)
            .flatMapIterable(sharedMember -> sharedMember)
            .next()
            .flatMap(this::getSharedMemberDetail);
    }

    private Mono<SharedMemberDetailDto> getSharedMemberDetail(SharedMemberEntity sharedMember) {
        return albumQuery.findById(sharedMember.getAlbumId())
            .flatMap(album -> memberServiceClient.getMemberInfoById(sharedMember.getMemberId())
                .map(memberInfo -> SharedMemberDetailDto.fromSharedMember(sharedMember, memberInfo, album))
            );
    }

    private boolean isShareStatusMatching(SharedMemberEntity sharedMember, ShareStatus shareStatus) {
        return shareStatus == null || sharedMember.getShareStatus().equals(shareStatus);
    }

    private boolean isPermissionLevelMatching(SharedMemberEntity sharedMember, PermissionLevel permissionLevel) {
        return permissionLevel == null || sharedMember.getPermissionLevel().equals(permissionLevel);
    }

    private Comparator<SharedMemberEntity> getComparator(SharedMemberSortType sort, SortOrder order) {
        Comparator<SharedMemberEntity> comparator = Comparator.comparing(SharedMemberEntity::getCreatedAt);

        if (DESC.equals(order)) {
            return comparator.reversed();
        } else {
            return comparator;
        }
    }

    @Transactional(readOnly = true)
    public Mono<SharedMemberEntity> findSharedMemberByAlbumIdAndMemberId(String albumId, String requestMemberId) {
        return sharedMemberQuery.findByAlbumIdAndMemberId(albumId, requestMemberId);
    }

    @Transactional
    public Mono<SharedMemberEntity> addSharedMember(String albumId, String permissionLevel, String sharingMemberId, String requestMemberId) {
        return albumPermissionVerifier.verifyOwnershipOrAccessPermission(albumId, requestMemberId, FULL_ACCESS)
            .flatMap(album -> sharedMemberQuery.checkDuplicateByAlbumIdAndMemberId(albumId, sharingMemberId)
                .then(sharedMemberCommand.addSharedMember(albumId, permissionLevel, Optional.empty(), sharingMemberId)
                    .flatMap(sharedMember -> memberServiceClient.getMemberInfoById(requestMemberId)
                        .flatMap(requestMember -> memberServiceClient
                            .sendScenarioNotification(
                                SHARED_MEMBER_INVITATION_CREATED,
                                List.of(sharingMemberId),
                                Map.of(
                                    "senderName", requestMember.name(),
                                    "shareTargetAlbumName", album.getName(),
                                    "sharedMemberId", sharedMember.getSharedMemberId()
                                )
                            )
                            .thenReturn(sharedMember)
                        )
                    )
                )
            );
    }

    @Transactional
    public Mono<Void> removeSharedMember(String sharedMemberId, String requestMemberId) {
        return sharedMemberQuery.findBySharedMemberId(sharedMemberId)
            .flatMap(sharedMember -> {
                if (isSharedMemberSelfRequest(sharedMember, requestMemberId)
                    // 공유 요청을 거절하는 경우, 삭제 실행하기로 하여 주석 처리
                    // && !isPendingStatus(sharedMember.getShareStatus())
                ) {
                    return sharedMemberCommand.removeSharedMember(sharedMember);
                } else {
                    return albumPermissionVerifier.verifyOwnership(sharedMember.getAlbumId(), requestMemberId)
                        .then(sharedMemberCommand.removeSharedMember(sharedMember));
                }
            });
    }

    @Transactional
    public Mono<SharedMemberEntity> modifySharedMemberShareStatus(String sharedMemberId, String newShareStatus, String requestMemberId) {
        return sharedMemberQuery.findBySharedMemberId(sharedMemberId)
            .flatMap(sharedMember -> {
                if (isSharedMemberSelfRequest(sharedMember, requestMemberId) && isPendingStatus(sharedMember.getShareStatus())) {
                    return sharedMemberCommand.modifySharedMemberShareStatus(sharedMember, newShareStatus);
                } else {
                    return Mono.error(new SharedMemberPermissionDeniedException());
                }
            });
    }

    @Transactional
    public Mono<SharedMemberEntity> modifySharedMemberShareStatusAccept(String sharedMemberId, String requestMemberId) {
        return sharedMemberQuery.findBySharedMemberId(sharedMemberId)
            .flatMap(sharedMember -> {
                if (isSharedMemberSelfRequest(sharedMember, requestMemberId) && isPendingStatus(sharedMember.getShareStatus())) {
                    return sharedMemberQuery.findAllByAlbumIdWhereStatusNotRejected(sharedMember.getAlbumId())
                        .collectList()
                        .flatMap(sharedMemberList -> {
                            List<String> receiverMemberIds = new java.util.ArrayList<>(sharedMemberList.stream().map(SharedMemberEntity::getMemberId).toList());
                            receiverMemberIds.remove(requestMemberId);

                            return sharedMemberCommand.modifySharedMemberShareStatus(sharedMember, ACCEPTED)
                                .then(memberServiceClient.getMemberInfoById(requestMemberId)
                                    .flatMap(memberDto -> albumQuery.findById(sharedMember.getAlbumId())
                                        .flatMap(album -> memberServiceClient
                                            .sendScenarioNotification(
                                                SHARED_MEMBER_INVITATION_ACCEPTED,
                                                receiverMemberIds,
                                                Map.of(
                                                    "shareTargetMemberName", album.getName(),
                                                    "shareTargetAlbumName", album.getName(),
                                                    "shareTargetAlbumId", sharedMember.getAlbumId()
                                                )
                                            )
                                        )
                                    )
                                )
                                .thenReturn(sharedMember);
                        });
                } else {
                    return Mono.error(new SharedMemberPermissionDeniedException());
                }
            });
    }

    @Transactional
    public Mono<SharedMemberEntity> modifySharedMemberShareStatusReject(String sharedMemberId, String requestMemberId) {
        return sharedMemberQuery.findBySharedMemberId(sharedMemberId)
            .flatMap(sharedMember -> {
                if (isSharedMemberSelfRequest(sharedMember, requestMemberId) && isPendingStatus(sharedMember.getShareStatus())) {
                    return sharedMemberCommand.modifySharedMemberShareStatus(sharedMember, REJECTED);
                } else {
                    return Mono.error(new SharedMemberPermissionDeniedException());
                }
            });
    }

    @Transactional
    public Mono<SharedMemberEntity> modifySharedMemberPermissionLevel(String sharedMemberId, String newPermissionLevel, String requestMemberId) {
        return sharedMemberQuery.findBySharedMemberId(sharedMemberId)
            .flatMap(sharedMember -> albumPermissionVerifier.verifyOwnership(sharedMember.getAlbumId(), requestMemberId)
                .then(sharedMemberCommand.modifySharedMemberPermissionLevel(sharedMember, newPermissionLevel))
            );
    }

    private boolean isSharedMemberSelfRequest(SharedMemberEntity sharedMember, String requestMemberId) {
        return sharedMember.getMemberId().equals(requestMemberId);
    }

    private boolean isPendingStatus(ShareStatus status) {
        return status.equals(PENDING);
    }

}

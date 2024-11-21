package kr.mafoo.photo.service;

import static kr.mafoo.photo.domain.enums.PermissionLevel.FULL_ACCESS;
import static kr.mafoo.photo.domain.enums.ShareStatus.PENDING;

import kr.mafoo.photo.domain.SharedMemberEntity;
import kr.mafoo.photo.domain.enums.ShareStatus;
import kr.mafoo.photo.exception.SharedMemberDuplicatedException;
import kr.mafoo.photo.exception.SharedMemberNotFoundException;
import kr.mafoo.photo.exception.SharedMemberPermissionDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class SharedMemberService {

    private final SharedMemberQuery sharedMemberQuery;
    private final SharedMemberCommand sharedMemberCommand;

    private final AlbumPermissionQuery albumPermissionQuery;

    @Transactional
    public Mono<SharedMemberEntity> addSharedMember(String albumId, String permissionLevel, String sharingMemberId, String requestMemberId) {
        return albumPermissionQuery.verifyOwnershipOrAccessPermission(albumId, requestMemberId, FULL_ACCESS)
            .then(sharedMemberQuery.findByAlbumIdAndMemberId(albumId, sharingMemberId)
                .onErrorResume(SharedMemberNotFoundException.class, ex ->
                    sharedMemberCommand.addSharedMember(albumId, permissionLevel, sharingMemberId)
                )
                .flatMap(existingMember -> Mono.error(new SharedMemberDuplicatedException()))
            );
    }

    @Transactional
    public Mono<Void> removeSharedMember(String sharedMemberId, String requestMemberId) {
        return sharedMemberQuery.findBySharedMemberId(sharedMemberId)
            .flatMap(sharedMember -> {
                if (isSharedMemberSelfRequest(sharedMember, requestMemberId) && !isPendingStatus(sharedMember.getShareStatus())) {
                    return sharedMemberCommand.removeSharedMember(sharedMember);
                } else {
                    return albumPermissionQuery.verifyOwnership(sharedMember.getAlbumId(), requestMemberId)
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
    public Mono<SharedMemberEntity> modifySharedMemberPermissionLevel(String sharedMemberId, String newPermissionLevel, String requestMemberId) {
        return sharedMemberQuery.findBySharedMemberId(sharedMemberId)
            .flatMap(sharedMember -> albumPermissionQuery.verifyOwnership(sharedMember.getAlbumId(), requestMemberId)
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

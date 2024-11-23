package kr.mafoo.photo.service;

import static kr.mafoo.photo.domain.enums.PermissionLevel.FULL_ACCESS;
import static kr.mafoo.photo.domain.enums.ShareStatus.PENDING;

import java.util.Optional;
import kr.mafoo.photo.domain.SharedMemberEntity;
import kr.mafoo.photo.domain.enums.ShareStatus;
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

    private final AlbumPermissionVerifier albumPermissionVerifier;

    @Transactional(readOnly = true)
    public Mono<SharedMemberEntity> findSharedMemberByAlbumIdAndMemberId(String albumId, String requestMemberId) {
        return sharedMemberQuery.findByAlbumIdAndMemberId(albumId, requestMemberId);
    }

    @Transactional
    public Mono<SharedMemberEntity> addSharedMember(String albumId, String permissionLevel, String sharingMemberId, String requestMemberId) {
        return albumPermissionVerifier.verifyOwnershipOrAccessPermission(albumId, requestMemberId, FULL_ACCESS)
            .then(sharedMemberQuery.checkDuplicateByAlbumIdAndMemberId(albumId, sharingMemberId)
                .then(sharedMemberCommand.addSharedMember(albumId, permissionLevel, Optional.empty(), sharingMemberId))
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

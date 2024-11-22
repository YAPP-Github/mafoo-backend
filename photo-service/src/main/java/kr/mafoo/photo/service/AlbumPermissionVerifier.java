package kr.mafoo.photo.service;

import static kr.mafoo.photo.domain.enums.ShareStatus.ACCEPTED;

import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.enums.PermissionLevel;
import kr.mafoo.photo.domain.enums.ShareStatus;
import kr.mafoo.photo.exception.AlbumOwnerMismatchException;
import kr.mafoo.photo.exception.SharedMemberNotFoundException;
import kr.mafoo.photo.exception.SharedMemberPermissionDeniedException;
import kr.mafoo.photo.exception.SharedMemberStatusNotAcceptedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AlbumPermissionVerifier {

    private final AlbumQuery albumQuery;
    private final SharedMemberQuery sharedMemberQuery;

    public Mono<AlbumEntity> verifyOwnershipOrAccessPermission(String albumId, String requestMemberId, PermissionLevel permissionLevel) {
        return verifyOwnership(albumId, requestMemberId)
            .onErrorResume(AlbumOwnerMismatchException.class, ownerEx ->
                sharedMemberQuery.findByAlbumIdAndMemberId(albumId, requestMemberId)
                    .onErrorResume(SharedMemberNotFoundException.class, sharedEx ->
                        Mono.error(new SharedMemberPermissionDeniedException())
                    )
                    .flatMap(sharedAlbumMember ->
                            checkShareStatus(sharedAlbumMember.getShareStatus())
                                .then(checkAccessPermission(sharedAlbumMember.getPermissionLevel(), permissionLevel))
                    ).then(albumQuery.findById(albumId))
            );
    }

    public Mono<AlbumEntity> verifyOwnership(String albumId, String requestMemberId) {
        return albumQuery.findById(albumId)
            .flatMap(album -> checkOwnership(album.getOwnerMemberId(), requestMemberId)
                .thenReturn(album)
            );
    }

    private Mono<Void> checkOwnership(String ownerMemberId, String requestMemberId) {
        if (!ownerMemberId.equals(requestMemberId)) {
            return Mono.error(new AlbumOwnerMismatchException());
        }
        return Mono.empty();
    }

    private Mono<Void> checkShareStatus(ShareStatus status) {
        if (!status.equals(ACCEPTED)) {
            return Mono.error(new SharedMemberStatusNotAcceptedException());
        }
        return Mono.empty();
    }

    private Mono<Void> checkAccessPermission(PermissionLevel currentLevel, PermissionLevel requiredLevel) {
        if (currentLevel.getTier() < requiredLevel.getTier()) {
            return Mono.error(new SharedMemberPermissionDeniedException());
        }
        return Mono.empty();
    }
}

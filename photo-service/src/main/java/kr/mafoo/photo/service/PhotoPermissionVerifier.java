package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.PhotoEntity;
import kr.mafoo.photo.domain.enums.PermissionLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PhotoPermissionVerifier {

    private final PhotoQuery photoQuery;
    private final AlbumPermissionVerifier albumPermissionVerifier;

    public Mono<PhotoEntity> verifyAccessPermission(String photoId, String requestMemberId, PermissionLevel permissionLevel) {
        return photoQuery.findByPhotoId(photoId)
            .flatMap(photo -> albumPermissionVerifier.verifyOwnershipOrAccessPermission(photo.getAlbumId(), requestMemberId, permissionLevel)
                .thenReturn(photo)
            );
    }

}

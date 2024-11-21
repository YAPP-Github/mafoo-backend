package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.PhotoEntity;
import kr.mafoo.photo.domain.enums.BrandType;
import kr.mafoo.photo.repository.PhotoRepository;
import kr.mafoo.photo.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class PhotoCommand {

    private final PhotoRepository photoRepository;

    public Mono<PhotoEntity> addPhotoWithoutOwnerAndAlbum(String photoUrl, BrandType type) {
        return photoRepository.save(
            PhotoEntity.newPhoto(IdGenerator.generate(), photoUrl, type, null, 0, null)
        );
    }

    public Mono<PhotoEntity> addPhoto(String fileLink, BrandType type, String albumId, Integer displayIndex, String ownerMemberId) {
        return photoRepository.save(
            PhotoEntity.newPhoto(IdGenerator.generate(), fileLink, type, albumId, displayIndex, ownerMemberId)
        );
    }

    public Mono<PhotoEntity> modifyPhotoAlbumId(PhotoEntity photo, String albumId, Integer newDisplayIndex, String ownerMemberId) {
        return photoRepository.save(
            photo.updateAlbumId(albumId)
                .updateOwnerMemberId(ownerMemberId)
                .updateDisplayIndex(newDisplayIndex)
        );
    }

    public Mono<Void> removePhoto(PhotoEntity photo) {
        return popDisplayIndexGreaterThan(photo.getAlbumId(), photo.getDisplayIndex())
            .then(photoRepository.delete(photo));
    }

    public Mono<Void> popDisplayIndexGreaterThan(String albumId, int startIndex) {
        return photoRepository.popDisplayIndexGreaterThan(albumId, startIndex);
    }

}

package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.PhotoEntity;
import kr.mafoo.photo.domain.enums.BrandType;
import kr.mafoo.photo.repository.PhotoRepository;
import kr.mafoo.photo.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
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

    public Mono<PhotoEntity> modifyPhotoDisplayIndex(PhotoEntity photo, Integer newDisplayIndex) {
        return photoRepository.save(
            photo.updateDisplayIndex(newDisplayIndex)
        );
    }

    public Flux<Void> modifyPhotosByAlbumIdToDecreaseDisplayIndexGreaterThan(String albumId, int startIndex) {
        return photoRepository.updateAllByAlbumIdToDecreaseDisplayIndexGreaterThan(albumId, startIndex);
    }

    public Flux<Void> modifyPhotosByAlbumIdToDecreaseDisplayIndexBetween(String albumId, int startIndex, int endIndex) {
        return photoRepository.updateAllByAlbumIdToDecreaseDisplayIndexBetween(albumId, startIndex, endIndex);
    }

    public Flux<Void> modifyPhotosByAlbumIdToIncreaseDisplayIndexBetween(String albumId, int startIndex, int endIndex) {
        return photoRepository.updateAllByAlbumIdToIncreaseDisplayIndexBetween(albumId, startIndex, endIndex);
    }

    public Mono<Void> removePhoto(PhotoEntity photo) {
        return modifyPhotosByAlbumIdToDecreaseDisplayIndexGreaterThan(photo.getAlbumId(), photo.getDisplayIndex())
            .then(photoRepository.softDeleteById(photo.getPhotoId()));
    }

    public Flux<Void> removePhotoByAlbumId(String albumId) {
        return photoRepository.softDeleteByAlbumId(albumId);
    }

}

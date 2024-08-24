package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.PhotoEntity;
import kr.mafoo.photo.exception.*;
import kr.mafoo.photo.repository.AlbumRepository;
import kr.mafoo.photo.repository.PhotoRepository;
import kr.mafoo.photo.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final AlbumRepository albumRepository;

    private final AlbumService albumService;
    private final QrService qrService;
    private final ObjectStorageService objectStorageService;

    @Transactional
    public Mono<PhotoEntity> createNewPhoto(String qrUrl, String requestMemberId) {
        return qrService
                .getFileFromQrUrl(qrUrl)
                .flatMap(fileDto -> objectStorageService.uploadFile(fileDto.fileByte())
                        .flatMap(photoUrl -> {
                            PhotoEntity photoEntity = PhotoEntity.newPhoto(IdGenerator.generate(), photoUrl, fileDto.type(), requestMemberId);
                            return photoRepository.save(photoEntity);
                        })
                );
    }

    @Transactional(readOnly = true)
    public Flux<PhotoEntity> findAllByAlbumId(String albumId, String requestMemberId) {
        return albumService.checkAlbumReadPermission(albumId, requestMemberId)
                .flatMapMany(albumEntity -> handleFindAllByAlbumId(albumEntity.getId()));
    }

    private Flux<PhotoEntity> handleFindAllByAlbumId(String albumId) {
        return photoRepository.findAllByAlbumIdOrderByDisplayIndexDesc(albumId);
    }

    @Transactional
    public Mono<Void> deletePhotoById(String photoId, String requestMemberId) {
        return photoRepository
                .findById(photoId)
                .switchIfEmpty(Mono.error(new PhotoNotFoundException()))
                .flatMap(photoEntity -> albumService
                        .checkAlbumFullAccessPermission(photoEntity.getAlbumId(), requestMemberId)
                        .flatMap(albumEntity -> handleDeletePhotoById(photoEntity)));
    }

    private Mono<Void> handleDeletePhotoById(PhotoEntity photoEntity) {
        return albumService.decreaseAlbumPhotoCount(photoEntity.getAlbumId())
                .then(photoRepository.popDisplayIndexGreaterThan(photoEntity.getAlbumId(), photoEntity.getDisplayIndex()))
                .then(photoRepository.deleteById(photoEntity.getPhotoId()));
    }

    @Transactional
    public Flux<PhotoEntity> updatePhotoBulkAlbumId(String[] photoIds, String albumId, String requestMemberId) {
        return Flux.fromArray(photoIds)
                .flatMap(photoId ->
                        this.updatePhotoAlbumId(photoId, albumId, requestMemberId)
                );
    }

    @Transactional
    public Mono<PhotoEntity> updatePhotoAlbumId(String photoId, String albumId, String requestMemberId) {
        return photoRepository
                .findById(photoId)
                .switchIfEmpty(Mono.error(new PhotoNotFoundException()))
                .flatMap(photoEntity -> {

                    if (photoEntity.getAlbumId() == null) {
                        if (!photoEntity.hasOwnerMemberId()) {
                            photoEntity.updateOwnerMemberId(requestMemberId);
                        }

                        return albumService.checkAlbumFullAccessPermission(albumId, requestMemberId)
                                .flatMap(albumEntity -> photoRepository.save(photoEntity.updateAlbumId(albumId)));
                    }

                    return albumService.checkAlbumFullAccessPermission(photoEntity.getAlbumId(), requestMemberId)
                            .flatMap(previousAlbum -> albumService.checkAlbumFullAccessPermission(albumId, requestMemberId)
                                    .flatMap(newAlbum -> handleUpdatePhotoAlbumId(photoEntity, newAlbum)));
                });
    }

    private Mono<PhotoEntity> handleUpdatePhotoAlbumId(PhotoEntity photoEntity, AlbumEntity albumEntity) {
        return albumService.decreaseAlbumPhotoCount(photoEntity.getAlbumId())
                .then(photoRepository.popDisplayIndexGreaterThan(photoEntity.getAlbumId(), photoEntity.getDisplayIndex()))
                .then(albumService.increaseAlbumPhotoCount(albumEntity.getId()))
                .then(photoRepository.save(
                        photoEntity.updateAlbumId(albumEntity.getId()).updateDisplayIndex(albumEntity.getPhotoCount())
                ));
    }

    @Transactional
    public Mono<PhotoEntity> updatePhotoDisplayIndex(String photoId, Integer newIndex, String requestMemberId) {
        return photoRepository
                .findById(photoId)
                .switchIfEmpty(Mono.error(new PhotoNotFoundException()))
                .flatMap(photoEntity -> albumService.checkAlbumFullAccessPermission(photoEntity.getAlbumId(), requestMemberId)
                        .flatMap(albumEntity -> {
                            int targetIndex = albumEntity.getPhotoCount() - newIndex - 1;

                            if (photoEntity.getDisplayIndex().equals(targetIndex)) {
                                return Mono.error(new PhotoDisplayIndexIsSameException());
                            }

                            if (targetIndex < 0 || targetIndex >= albumEntity.getPhotoCount()) {
                                return Mono.error(new PhotoDisplayIndexNotValidException());
                            }

                            if (photoEntity.getDisplayIndex() < targetIndex) {
                                return photoRepository
                                        .popDisplayIndexBetween(photoEntity.getAlbumId(), photoEntity.getDisplayIndex() + 1, targetIndex)
                                        .then(photoRepository.save(photoEntity.updateDisplayIndex(targetIndex)));
                            } else {
                                return photoRepository
                                        .pushDisplayIndexBetween(photoEntity.getAlbumId(), targetIndex, photoEntity.getDisplayIndex() - 1)
                                        .then(photoRepository.save(photoEntity.updateDisplayIndex(targetIndex)));
                            }
                        }));
    }

}

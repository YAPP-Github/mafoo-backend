package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.PhotoEntity;
import kr.mafoo.photo.exception.AlbumNotFoundException;
import kr.mafoo.photo.exception.PhotoNotFoundException;
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

    public Flux<PhotoEntity> findAllByAlbumId(String albumId, String requestMemberId) {
        return albumRepository
                .findById(albumId)
                .switchIfEmpty(Mono.error(new AlbumNotFoundException()))
                .flatMapMany(albumEntity -> {
                    if (!albumEntity.getOwnerMemberId().equals(requestMemberId)) {
                        // 내 앨범이 아니면 그냥 없는 앨범 처리
                        return Mono.error(new AlbumNotFoundException());
                    } else {
                        return photoRepository.findAllByAlbumIdOrderByDisplayIndexDesc(albumId);
                    }
                });
    }

    @Transactional
    public Mono<Void> deletePhotoById(String photoId, String requestMemberId) {
        return photoRepository
                .findById(photoId)
                .switchIfEmpty(Mono.error(new PhotoNotFoundException()))
                .flatMap(photoEntity -> {
                    if (!photoEntity.getOwnerMemberId().equals(requestMemberId)) {
                        // 내 사진이 아니면 그냥 없는 사진 처리
                        return Mono.error(new PhotoNotFoundException());
                    } else {
                        return albumService.decreaseAlbumPhotoCount(photoEntity.getAlbumId(), requestMemberId)
                                .then(photoRepository.popDisplayIndexGreaterThan(photoEntity.getAlbumId(), photoEntity.getDisplayIndex()))
                                .then(photoRepository.deleteById(photoId));
                    }
                });
    }

    @Transactional
    public Mono<PhotoEntity> updatePhotoAlbumId(String photoId, String albumId, String requestMemberId) {
        return photoRepository
                .findById(photoId)
                .switchIfEmpty(Mono.error(new PhotoNotFoundException()))
                .flatMap(photoEntity -> {

                    if (!photoEntity.hasOwnerMemberId()) {
                        photoRepository.save(photoEntity.updateOwnerMemberId(requestMemberId));
                    }

                    if (!photoEntity.getOwnerMemberId().equals(requestMemberId)) {
                        // 내 사진이 아니면 그냥 없는 사진 처리
                        return Mono.error(new PhotoNotFoundException());
                    } else {
                        return albumRepository
                                .findById(albumId)
                                .switchIfEmpty(Mono.error(new AlbumNotFoundException()))
                                .flatMap(albumEntity -> {
                                    if (!albumEntity.getOwnerMemberId().equals(requestMemberId)) {
                                        // 내 앨범이 아니면 그냥 없는 앨범 처리
                                        return Mono.error(new AlbumNotFoundException());
                                    } else {
                                        return albumService.decreaseAlbumPhotoCount(photoEntity.getAlbumId(), requestMemberId)
                                                .then(photoRepository.popDisplayIndexGreaterThan(photoEntity.getAlbumId(), photoEntity.getDisplayIndex()))
                                                .then(albumService.increaseAlbumPhotoCount(albumId, requestMemberId))
                                                .then(photoRepository.save(
                                                        photoEntity.updateAlbumId(albumId)
                                                                .updateDisplayIndex(albumEntity.getPhotoCount())
                                                        )
                                                );
                                    }
                                });
                    }
                });
    }

    @Transactional
    public Mono<PhotoEntity> updatePhotoDisplayIndex(String photoId, Integer newIndex, String requestMemberId) {
        return photoRepository
                .findById(photoId)
                .switchIfEmpty(Mono.error(new PhotoNotFoundException()))
                .flatMap(photoEntity -> albumRepository
                        .findById(photoEntity.getAlbumId())
                        .switchIfEmpty(Mono.error(new AlbumNotFoundException()))
                        .flatMap(albumEntity -> {

                            if (photoEntity.getDisplayIndex() < newIndex) {
                                return photoRepository
                                        .popDisplayIndexBetween(photoEntity.getAlbumId(), photoEntity.getDisplayIndex() + 1, newIndex)
                                        .then(photoRepository.save(photoEntity.updateDisplayIndex(newIndex)));
                            } else {
                                return photoRepository
                                        .pushDisplayIndexBetween(photoEntity.getAlbumId(), newIndex, photoEntity.getDisplayIndex() - 1)
                                        .then(photoRepository.save(photoEntity.updateDisplayIndex(newIndex)));
                            }

                        }));
    }

}

package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.PhotoApi;
import kr.mafoo.photo.controller.dto.request.PhotoCreateRequest;
import kr.mafoo.photo.controller.dto.request.PhotoBulkUpdateAlbumIdRequest;
import kr.mafoo.photo.controller.dto.request.PhotoUpdateAlbumIdRequest;
import kr.mafoo.photo.controller.dto.request.PhotoUpdateDisplayIndexRequest;
import kr.mafoo.photo.controller.dto.response.PhotoResponse;
import kr.mafoo.photo.domain.AlbumType;
import kr.mafoo.photo.domain.PhotoEntity;
import kr.mafoo.photo.exception.AlbumNotFoundException;
import kr.mafoo.photo.repository.AlbumRepository;
import kr.mafoo.photo.repository.PhotoRepository;
import kr.mafoo.photo.service.AlbumService;
import kr.mafoo.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class PhotoController implements PhotoApi {

    private final PhotoService photoService;
    private final AlbumService albumService;
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    @Override
    public Flux<PhotoResponse> getPhotos(
            String memberId,
            String albumId
    ){
        return photoService
                .findAllByAlbumId(albumId, memberId)
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Mono<PhotoResponse> createPhoto(
            String memberId,
            PhotoCreateRequest request
    ){
        return photoService
                .createNewPhoto(request.qrUrl(), memberId)
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Flux<PhotoResponse> uploadPhoto(String memberId, Flux<FilePart> request) {
        return photoService
                .uploadPhoto(request, memberId)
                .map(PhotoResponse::fromEntity);
    }

    @Transactional
    @Override
    public Flux<PhotoResponse> uploadPhotoMafoo(String memberId, Flux<FilePart> request, String albumName) {
        return albumService
                .createNewAlbum(memberId, albumName, AlbumType.SMILE_FACE)
                .flatMapMany(albumEntity -> {
                    return photoService
                            .uploadPhoto(request, memberId)
                            .flatMap(photoEntity -> {
                                photoEntity.setNew(false);
                                return albumService.decreaseAlbumPhotoCount(photoEntity.getAlbumId(), memberId)
                                        .then(photoRepository.popDisplayIndexGreaterThan(photoEntity.getAlbumId(), photoEntity.getDisplayIndex()))
                                        .then(albumService.increaseAlbumPhotoCount(albumEntity.getAlbumId(), memberId))
                                        .then(photoRepository.save(
                                                photoEntity
                                                        .updateAlbumId(albumEntity.getAlbumId())
                                                        .updateDisplayIndex(albumEntity.getPhotoCount())
                                        ));
                            });

                })
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Mono<PhotoResponse> updatePhotoAlbum(
            String memberId,
            String photoId,
            PhotoUpdateAlbumIdRequest request
    ){
        return photoService
                .updatePhotoAlbumId(photoId, request.albumId(), memberId)
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Flux<PhotoResponse> updatePhotoBulkAlbum(
            String memberId,
            PhotoBulkUpdateAlbumIdRequest request
    ){
        return photoService
                .updatePhotoBulkAlbumId(request.photoIds(), request.albumId(), memberId)
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Mono<PhotoResponse> updatePhotoDisplayIndex(
            String memberId,
            String photoId,
            PhotoUpdateDisplayIndexRequest request
    ) {
        return photoService
                .updatePhotoDisplayIndex(photoId, request.newDisplayIndex(), memberId)
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Mono<Void> deletePhoto(
            String memberId,
            String photoId
    ){
        return photoService
                .deletePhotoById(photoId, memberId);
    }
}

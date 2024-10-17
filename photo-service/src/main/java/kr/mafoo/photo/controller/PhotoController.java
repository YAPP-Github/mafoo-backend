package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.PhotoApi;
import kr.mafoo.photo.controller.dto.request.PhotoCreateRequest;
import kr.mafoo.photo.controller.dto.request.PhotoBulkUpdateAlbumIdRequest;
import kr.mafoo.photo.controller.dto.request.PhotoUpdateAlbumIdRequest;
import kr.mafoo.photo.controller.dto.request.PhotoUpdateDisplayIndexRequest;
import kr.mafoo.photo.controller.dto.response.PhotoResponse;
import kr.mafoo.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class PhotoController implements PhotoApi {

    private final PhotoService photoService;

    @Override
    public Flux<PhotoResponse> getPhotos(
            String memberId,
            String albumId,
            String sort
    ){
        return photoService
                .findAllByAlbumId(albumId, memberId, sort)
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

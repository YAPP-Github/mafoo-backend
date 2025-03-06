package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.PhotoApi;
import kr.mafoo.photo.controller.dto.request.*;
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
    public Flux<PhotoResponse> getPhotoListByAlbum(
            String memberId,
            String albumId,
            String cursor,
            Integer size
    ){
        int actualSize = size != null ? size : 50;
        return photoService
                .paginatePhotoById(albumId, memberId, cursor, actualSize)
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Mono<PhotoResponse> createPhotoWithQrUrlOriginal(
            String memberId,
            PhotoCreateWithQrUrlRequest request
    ){
        return photoService
                .addPhotoWithQrUrl(request.qrUrl())
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Mono<PhotoResponse> createPhotoWithQrUrl(
            String memberId,
            PhotoCreateWithQrUrlRequest request
    ){
        return photoService
                .addPhotoWithQrUrl(request.qrUrl())
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Flux<PhotoResponse> createPhotoBulkWithFileUrls(
            String memberId,
            PhotoCreateBulkWithFileUrlsRequest request
    ){
        return photoService
                .addPhotoBulkWithFileUrls(request.fileUrls(), request.albumId(), memberId)
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Flux<PhotoResponse> uploadPhoto(String memberId, Flux<FilePart> request) {
        return photoService
                .uploadPhoto(request, memberId)
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Mono<PhotoResponse> setPhotoAlbum(
            String memberId,
            String photoId,
            PhotoSetAlbumRequest request
    ){
        return photoService
                .initPhotoAlbumId(photoId, request.albumId(), memberId)
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Flux<PhotoResponse> updatePhotoBulkAlbum(
            String memberId,
            PhotoUpdateBulkAlbumRequest request
    ){
        return photoService
                .modifyPhotoBulkAlbumId(request.photoIds(), request.albumId(), memberId)
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Mono<PhotoResponse> updatePhotoDisplayIndex(
            String memberId,
            String photoId,
            PhotoUpdateDisplayIndexRequest request
    ) {
        return photoService
                .modifyPhotoDisplayIndex(photoId, request.newDisplayIndex(), memberId)
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Mono<Void> deletePhoto(
            String memberId,
            String photoId
    ){
        return photoService
                .removePhoto(photoId, memberId);
    }
}

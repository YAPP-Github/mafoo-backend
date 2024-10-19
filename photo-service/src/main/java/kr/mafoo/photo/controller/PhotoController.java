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
    public Flux<PhotoResponse> getPhotos(
            String memberId,
            String albumId
    ){
        return photoService
                .findAllByAlbumId(albumId, memberId)
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Mono<PhotoResponse> uploadQrPhotoOriginal(
            String memberId,
            PhotoQrUploadRequest request
    ){
        return photoService
                .createNewPhotoByQrUrl(request.qrUrl(), memberId)
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Mono<PhotoResponse> uploadQrPhoto(
            String memberId,
            PhotoQrUploadRequest request
    ){
        return photoService
                .createNewPhotoByQrUrl(request.qrUrl(), memberId)
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Flux<PhotoResponse> uploadFileUrlPhoto(
            String memberId,
            PhotoFileUrlUploadRequest request
    ){
        return photoService
                .createNewPhotoFileUrl(request.fileUrls(), memberId)
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

package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.PhotoApi;
import kr.mafoo.photo.controller.dto.request.PhotoCreateRequest;
import kr.mafoo.photo.controller.dto.request.PhotoUpdateAlbumIdRequest;
import kr.mafoo.photo.controller.dto.response.PhotoResponse;
import kr.mafoo.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
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
                .findAllByOwnerAlbumId(albumId, memberId)
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Mono<PhotoResponse> createPhoto(
            String memberId,
            PhotoCreateRequest request
    ){
        return Mono.just(
                new PhotoResponse("test_photo_id", "photo_url", null)
        );
    }

    @Override
    public Mono<PhotoResponse> updatePhotoAlbum(
            String memberId,
            String photoId,
            PhotoUpdateAlbumIdRequest request
    ){
        return photoService
                .updatePhotoOwnerAlbumId(photoId, request.albumId(), memberId)
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

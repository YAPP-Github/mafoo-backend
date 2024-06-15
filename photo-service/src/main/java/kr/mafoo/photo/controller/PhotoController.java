package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.PhotoApi;
import kr.mafoo.photo.controller.dto.request.PhotoCreateRequest;
import kr.mafoo.photo.controller.dto.request.PhotoAlbumUpdateRequest;
import kr.mafoo.photo.controller.dto.response.PhotoResponse;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class PhotoController implements PhotoApi {

    @Override
    public Flux<PhotoResponse> getAlbumPhotos(
            String albumId
    ){
        return Flux.just(
                new PhotoResponse("test_photo_id_a", "test_album_id_a", "photo_url"),
                new PhotoResponse("test_photo_id_b", "test_album_id_a", "photo_url"),
                new PhotoResponse("test_photo_id_c", "test_album_id_a", "photo_url")
        );
    }

    @Override
    public Mono<PhotoResponse> createPhoto(
            PhotoCreateRequest request
    ){
        return Mono.just(
                new PhotoResponse("test_photo_id", "photo_url", null)
        );
    }

    @Override
    public Mono<PhotoResponse> updatePhotoAlbum(
            String photoId,
            PhotoAlbumUpdateRequest request
    ){
        return Mono.just(
                new PhotoResponse("test_photo_id", "photo_url", "test_album_id")
        );
    }

    @Override
    public Mono<Void> deletePhoto(
            String photoId
    ){
        return Mono.empty();
    }
}

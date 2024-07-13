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

import static kr.mafoo.photo.domain.BrandType.LIFE_FOUR_CUTS;

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
    public Mono<PhotoResponse> createPhoto(
            String memberId,
            PhotoCreateRequest request
    ){

        // HACK : 프엔 응급 요청으로 더미 응답 추가했습니다. - 추후 삭제 예정
        if (request.qrUrl().equals("https://mafoo.kr/")) {
            return Mono.just(new PhotoResponse("id", "https://kr.object.ncloudstorage.com/mafoo//24c576bc-60b0-4e43-af9a-0c3311b97f35", LIFE_FOUR_CUTS, null));
        }

        return photoService
                .createNewPhoto(request.qrUrl(), memberId)
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
    public Mono<Void> deletePhoto(
            String memberId,
            String photoId
    ){
        return photoService
                .deletePhotoById(photoId, memberId);
    }
}

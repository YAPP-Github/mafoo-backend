package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.AdminApi;
import kr.mafoo.photo.controller.dto.response.AlbumRawResponse;
import kr.mafoo.photo.controller.dto.response.PageResponse;
import kr.mafoo.photo.controller.dto.response.PhotoResponse;
import kr.mafoo.photo.service.AlbumAdminService;
import kr.mafoo.photo.service.PhotoAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class AdminController implements AdminApi {
    private final AlbumAdminService albumAdminService;
    private final PhotoAdminService photoAdminService;

    @Override
    public Mono<PageResponse<AlbumRawResponse>> queryAlbums(int page, int size, String name, String type, String ownerMemberId) {
        return albumAdminService
                .queryAlbums(page, size, name, type, ownerMemberId)
                .map(pageResponse -> pageResponse.map(AlbumRawResponse::fromEntity));
    }

    @Override
    public Mono<PageResponse<PhotoResponse>> getPhotos(int page, int size, String type, String ownerMemberId) {
        return photoAdminService
                .queryPhotos(page, size, type, ownerMemberId)
                .map(pageResponse -> pageResponse.map(PhotoResponse::fromEntity));
    }


}

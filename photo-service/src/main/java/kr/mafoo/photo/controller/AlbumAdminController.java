package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.AlbumAdminApi;
import kr.mafoo.photo.controller.dto.response.AlbumRawResponse;
import kr.mafoo.photo.controller.dto.response.PageResponse;
import kr.mafoo.photo.service.AlbumAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class AlbumAdminController implements AlbumAdminApi {
    private final AlbumAdminService albumAdminService;

    @Override
    public Mono<PageResponse<AlbumRawResponse>> queryAlbums(int page, int size, String name, String type, String ownerMemberId) {
        return albumAdminService
                .queryAlbums(page, size, name, type, ownerMemberId)
                .map(pageResponse -> pageResponse.map(AlbumRawResponse::fromEntity));
    }
}

package kr.mafoo.photo.api;

import kr.mafoo.photo.controller.dto.response.AlbumResponse;
import kr.mafoo.photo.controller.dto.response.PageResponse;
import kr.mafoo.photo.controller.dto.response.PhotoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@RequestMapping("/v1/admin")
public interface AdminApi {
    @GetMapping("/albums")
    Mono<PageResponse<AlbumResponse>> queryAlbums(
            @RequestParam(defaultValue = "1")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(required = false)
            String name,

            @RequestParam(required = false)
            String type,

            @RequestParam(required = false)
            String ownerMemberId
    );

    @GetMapping("/photos")
    Mono<PageResponse<PhotoResponse>> getPhotos(
            @RequestParam(defaultValue = "1")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(required = false)
            String type,

            @RequestParam(required = false)
            String ownerMemberId
    );
}

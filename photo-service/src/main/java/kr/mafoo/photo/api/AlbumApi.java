package kr.mafoo.photo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.mafoo.photo.controller.dto.request.AlbumCreateRequest;
import kr.mafoo.photo.controller.dto.request.AlbumUpdateRequest;
import kr.mafoo.photo.controller.dto.response.AlbumResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "앨범 관련 API", description = "앨범 조회, 생성, 수정, 삭제 등 API")
@RequestMapping("/v1/albums")
public interface AlbumApi {
    @Operation(summary = "앨범 조회", description = "앨범 목록을 조회합니다.")
    @GetMapping
    Flux<AlbumResponse> getAlbums(
    );

    @Operation(summary = "앨범 생성", description = "앨범을 생성합니다.")
    @PostMapping
    Mono<AlbumResponse> createAlbum(
            @RequestBody
            AlbumCreateRequest request
    );

    @Operation(summary = "앨범 수정", description = "앨범의 이름 및 종류를 수정합니다.")
    @PutMapping("/{albumId}")
    Mono<AlbumResponse> updateAlbum(
            @Parameter(description = "앨범 ID", example = "test_album_id")
            @PathVariable
            String albumId,

            @RequestBody
            AlbumUpdateRequest request
    );

    @Operation(summary = "앨범 삭제", description = "앨범을 삭제합니다.")
    @DeleteMapping("/{albumId}")
    Mono<Void> deleteAlbum(
            @Parameter(description = "앨범 ID", example = "test_album_id")
            @PathVariable
            String albumId
    );
}

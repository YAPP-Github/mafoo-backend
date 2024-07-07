package kr.mafoo.photo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.mafoo.photo.annotation.RequestMemberId;
import kr.mafoo.photo.controller.dto.request.AlbumCreateRequest;
import kr.mafoo.photo.controller.dto.request.AlbumUpdateRequest;
import kr.mafoo.photo.controller.dto.response.AlbumResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "앨범 관련 API", description = "앨범 조회, 생성, 수정, 삭제 등 API")
@RequestMapping("/v1/albums")
public interface AlbumApi {
    @Operation(summary = "앨범 n건 조회", description = "앨범 목록을 조회합니다.")
    @GetMapping
    Flux<AlbumResponse> getAlbums(
            @RequestMemberId
            String memberId
    );

    @Operation(summary = "앨범 단건 조회", description = "앨범 단건을 조회합니다.")
    @GetMapping("/{albumId}")
    Mono<AlbumResponse> getAlbum(
            @RequestMemberId
            String memberId,

            @Parameter(description = "앨범 ID", example = "test_album_id")
            @PathVariable
            String albumId
    );

    @Operation(summary = "앨범 생성", description = "앨범을 생성합니다.")
    @PostMapping
    Mono<AlbumResponse> createAlbum(
            @RequestMemberId
            String memberId,

            @RequestBody
            AlbumCreateRequest request
    );

    @Operation(summary = "앨범 변경", description = "앨범의 속성을 변경합니다.")
    @PutMapping("/{albumId}")
    Mono<AlbumResponse> updateAlbum(
            @RequestMemberId
            String memberId,

            @Parameter(description = "앨범 ID", example = "test_album_id")
            @PathVariable
            String albumId,

            @RequestBody
            AlbumUpdateRequest request
    );

    @Operation(summary = "앨범 삭제", description = "앨범을 삭제합니다.")
    @DeleteMapping("/{albumId}")
    Mono<Void> deleteAlbum(
            @RequestMemberId
            String memberId,

            @Parameter(description = "앨범 ID", example = "test_album_id")
            @PathVariable
            String albumId
    );
}

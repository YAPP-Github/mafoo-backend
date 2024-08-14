package kr.mafoo.photo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.mafoo.photo.annotation.RequestMemberId;
import kr.mafoo.photo.annotation.ULID;
import kr.mafoo.photo.controller.dto.request.AlbumCreateRequest;
import kr.mafoo.photo.controller.dto.request.AlbumUpdateDisplayIndexRequest;
import kr.mafoo.photo.controller.dto.request.AlbumUpdateRequest;
import kr.mafoo.photo.controller.dto.response.AlbumResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
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

            @ULID
            @Parameter(description = "앨범 ID", example = "test_album_id")
            @PathVariable
            String albumId
    );

    @Operation(summary = "앨범 생성", description = "앨범을 생성합니다.")
    @PostMapping
    Mono<AlbumResponse> createAlbum(
            @RequestMemberId
            String memberId,

            @Valid
            @RequestBody
            AlbumCreateRequest request
    );

    @Operation(summary = "앨범 변경", description = "앨범의 속성을 변경합니다.")
    @PutMapping("/{albumId}")
    Mono<AlbumResponse> updateAlbum(
            @RequestMemberId
            String memberId,

            @ULID
            @Parameter(description = "앨범 ID", example = "test_album_id")
            @PathVariable
            String albumId,

            @Valid
            @RequestBody
            AlbumUpdateRequest request
    );

    @Operation(summary = "앨범 표기 순서 변경", description = "앨범의 표기 순서를 변경합니다.")
    @PatchMapping("/{albumId}/display-index")
    Mono<AlbumResponse> updateAlbumDisplayIndex(
            @RequestMemberId
            String memberId,

            @ULID
            @Parameter(description = "앨범 ID", example = "test_album_id")
            @PathVariable
            String albumId,

            @Valid
            @RequestBody
            AlbumUpdateDisplayIndexRequest request
    );

    @Operation(summary = "앨범 삭제", description = "앨범을 삭제합니다.")
    @DeleteMapping("/{albumId}")
    Mono<Void> deleteAlbum(
            @RequestMemberId
            String memberId,

            @ULID
            @Parameter(description = "앨범 ID", example = "test_album_id")
            @PathVariable
            String albumId
    );
}

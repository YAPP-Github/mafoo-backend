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
import kr.mafoo.photo.controller.dto.response.AlbumRawResponse;
import kr.mafoo.photo.controller.dto.response.AlbumResponse;
import kr.mafoo.photo.controller.dto.response.PageResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@Tag(name = "앨범 어드민 관련 API", description = "앨범 조회, 생성, 수정, 삭제 등 API")
@RequestMapping("/v1/admin/albums")
public interface AlbumAdminApi {
    @Operation(summary = "앨범 n건 조회", description = "앨범 목록을 조회합니다.")
    @GetMapping
    Mono<PageResponse<AlbumRawResponse>> queryAlbums(
            @Parameter(description = "페이지 번호", example = "1")
            @RequestParam(defaultValue = "1")
            int page,

            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(required = false)
            String name,

            @RequestParam(required = false)
            String type,

            @RequestParam(required = false)
            String ownerMemberId
    );

//    @Operation(summary = "앨범 생성", description = "앨범을 생성합니다.")
//    @PostMapping
//    Mono<AlbumResponse> createAlbum(
//            @Valid
//            @RequestBody
//            AlbumCreateRequest request
//    );
//
//    @Operation(summary = "앨범 변경", description = "앨범의 속성을 변경합니다.")
//    @PutMapping("/{albumId}")
//    Mono<AlbumResponse> updateAlbum(
//            @RequestMemberId
//            String memberId,
//
//            @ULID
//            @Parameter(description = "앨범 ID", example = "test_album_id")
//            @PathVariable
//            String albumId,
//
//            @Valid
//            @RequestBody
//            AlbumUpdateRequest request
//    );
//
//    @Operation(summary = "앨범 삭제", description = "앨범을 삭제합니다.")
//    @DeleteMapping("/{albumId}")
//    Mono<Void> deleteAlbum(
//            @RequestMemberId
//            String memberId,
//
//            @ULID
//            @Parameter(description = "앨범 ID", example = "test_album_id")
//            @PathVariable
//            String albumId
//    );
}

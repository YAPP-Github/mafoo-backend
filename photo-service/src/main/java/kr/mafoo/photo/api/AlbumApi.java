package kr.mafoo.photo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.mafoo.photo.annotation.RequestMemberId;
import kr.mafoo.photo.annotation.ULID;
import kr.mafoo.photo.controller.dto.request.AlbumCreateRequest;
import kr.mafoo.photo.controller.dto.request.AlbumUpdateNameAndTypeRequest;
import kr.mafoo.photo.controller.dto.request.AlbumUpdateOwnershipRequest;
import kr.mafoo.photo.controller.dto.response.ViewableAlbumResponse;
import kr.mafoo.photo.controller.dto.response.AlbumResponse;
import kr.mafoo.photo.controller.dto.response.ViewableAlbumDetailResponse;
import kr.mafoo.photo.domain.enums.AlbumSortType;
import kr.mafoo.photo.domain.enums.AlbumType;
import kr.mafoo.photo.domain.enums.SortOrder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@Tag(name = "앨범 관련 API", description = "앨범 조회, 생성, 변경, 삭제 등 API")
@RequestMapping("/v1/albums")
public interface AlbumApi {
    @Operation(summary = "사용자 별 앨범 목록 조회", description = "사용자 별 앨범 목록을 조회합니다.")
    @GetMapping
    Flux<ViewableAlbumResponse> getAlbumListByMember(
            @RequestMemberId
            String memberId
    );

    @GetMapping("/variables")
    Mono<ViewableAlbumResponse> getAlbumVariables(
        @RequestParam(required = false)
        AlbumType albumType,

        @RequestParam
        AlbumSortType sort,

        @RequestParam
        SortOrder order,

        @RequestParam
        String memberId
    );

    @Operation(summary = "앨범 단건 조회", description = "앨범 단건을 조회합니다.")
    @GetMapping("/{albumId}")
    Mono<ViewableAlbumDetailResponse> getAlbum(
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

    @Operation(summary = "앨범 속성(이름, 종류) 변경", description = "앨범의 속성(이름, 종류)을 변경합니다.")
    @PutMapping("/{albumId}")
    Mono<AlbumResponse> updateAlbumNameAndType(
            @RequestMemberId
            String memberId,

            @ULID
            @Parameter(description = "앨범 ID", example = "test_album_id")
            @PathVariable
            String albumId,

            @Valid
            @RequestBody
            AlbumUpdateNameAndTypeRequest request
    );

//    @Operation(summary = "[DEPRECATED] 앨범 표시 순서 변경", description = "앨범의 표시 순서를 변경합니다.")
//    @PatchMapping("/{albumId}/display-index")
//    Mono<AlbumResponse> updateAlbumDisplayIndex(
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
//            AlbumUpdateDisplayIndexRequest request
//    );

    @Operation(summary = "앨범 소유자 변경", description = "앨범의 소유자를 변경합니다.")
    @PatchMapping("/{albumId}/ownership")
    Mono<AlbumResponse> updateAlbumOwnerShip(
            @RequestMemberId
            String memberId,

            @ULID
            @Parameter(description = "앨범 ID", example = "test_album_id")
            @PathVariable
            String albumId,

            @Valid
            @RequestBody
            AlbumUpdateOwnershipRequest request
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

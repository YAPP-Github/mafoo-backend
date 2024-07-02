package kr.mafoo.photo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.mafoo.photo.annotation.RequestMemberId;
import kr.mafoo.photo.controller.dto.request.PhotoCreateRequest;
import kr.mafoo.photo.controller.dto.request.PhotoUpdateAlbumIdRequest;
import kr.mafoo.photo.controller.dto.response.PhotoResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "사진 관련 API", description = "사진 조회, 생성, 수정, 삭제 등 API")
@RequestMapping("/v1/photos")
public interface PhotoApi {
    @Operation(summary = "사진 조회", description = "사진 목록을 조회합니다.")
    @GetMapping
    Flux<PhotoResponse> getPhotos(
            @RequestMemberId
            String memberId,

            @Parameter(description = "앨범 ID", example = "test_album_id")
            @RequestParam
            String albumId
    );

    @Operation(summary = "사진 생성", description = "사진을 생성합니다.")
    @PostMapping
    Mono<PhotoResponse> createPhoto(
            @RequestMemberId
            String memberId,

            @RequestBody
            PhotoCreateRequest request
    );

    @Operation(summary = "사진 앨범 수정", description = "사진을 다른 앨범으로 이동시킵니다.")
    @PatchMapping("/{photoId}/album")
    Mono<PhotoResponse> updatePhotoAlbum(
            @RequestMemberId
            String memberId,

            @Parameter(description = "사진 ID", example = "test_photo_id")
            @PathVariable
            String photoId,

            @RequestBody
            PhotoUpdateAlbumIdRequest request
    );

    @Operation(summary = "사진 삭제", description = "사진을 삭제합니다.")
    @DeleteMapping("{photoId}")
    Mono<Void> deletePhoto(
            @RequestMemberId
            String memberId,

            @Parameter(description = "사진 ID", example = "test_photo_id")
            @PathVariable
            String photoId
    );
}

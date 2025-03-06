package kr.mafoo.photo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.mafoo.photo.annotation.RequestMemberId;
import kr.mafoo.photo.annotation.ULID;
import kr.mafoo.photo.controller.dto.request.*;
import kr.mafoo.photo.controller.dto.response.PhotoResponse;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@Tag(name = "사진 관련 API", description = "사진 조회, 생성, 수정, 삭제 등 API")
@RequestMapping("/v1/photos")
public interface PhotoApi {
    @Operation(summary = "사진 조회", description = "사진 목록을 조회합니다.")
    @GetMapping
    Flux<PhotoResponse> getPhotoListByAlbum(
            @RequestMemberId
            String memberId,

            @ULID
            @Parameter(description = "앨범 ID", example = "test_album_id")
            @RequestParam
            String albumId,

            @Parameter(description = "커서", example = "image_id")
            @RequestParam(required = false)
            String cursor,

            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(required = false)
            Integer size
    );

    @Operation(summary = "(수정 이전) QR 사진 업로드", description = "QR을 사용해 사진을 업로드합니다.")
    @PostMapping(value = "")
    Mono<PhotoResponse> createPhotoWithQrUrlOriginal(
            @RequestMemberId
            String memberId,

            @Valid
            @RequestBody
            PhotoCreateWithQrUrlRequest request
    );

    @Operation(summary = "QR 사진 업로드", description = "QR을 사용해 사진을 업로드합니다.")
    @PostMapping(value = "/qr")
    Mono<PhotoResponse> createPhotoWithQrUrl(
            @RequestMemberId
            String memberId,

            @Valid
            @RequestBody
            PhotoCreateWithQrUrlRequest request
    );

    @Operation(summary = "파일(url) 사진 n건 업로드", description = "파일(url)을 사용해 사진을 업로드합니다.")
    @PostMapping(value = "/file-urls")
    Flux<PhotoResponse> createPhotoBulkWithFileUrls(
            @RequestMemberId
            String memberId,

            @Valid
            @RequestBody
            PhotoCreateBulkWithFileUrlsRequest request
    );

    @Operation(summary = "사진 파일로 업로드", description = "사진을 직접 업로드합니다.")
    @PostMapping(value = "/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Flux<PhotoResponse> uploadPhoto(
            @RequestMemberId
            String memberId,

            @RequestPart("files")
            Flux<FilePart> request
    );

    @Operation(summary = "사진 앨범 설정", description = "사진의 초기 앨범 정보를 설정합니다.")
    @PatchMapping(value = "/{photoId}/album")
    Mono<PhotoResponse> setPhotoAlbum(
            @RequestMemberId
            String memberId,

            @ULID
            @Parameter(description = "사진 ID", example = "test_photo_id")
            @PathVariable
            String photoId,

            @Valid
            @RequestBody
            PhotoSetAlbumRequest request
    );

    @Operation(summary = "사진 앨범 n건 수정", description = "사진 여러 개를 다른 앨범으로 이동시킵니다.")
    @PatchMapping("/bulk/album")
    Flux<PhotoResponse> updatePhotoBulkAlbum(
            @RequestMemberId
            String memberId,

            @Valid
            @RequestBody
            PhotoUpdateBulkAlbumRequest request
    );

    @Operation(summary = "사진 표시 순서 변경", description = "사진의 표시 순서를 변경합니다.")
    @PatchMapping("/{photoId}/display-index")
    Mono<PhotoResponse> updatePhotoDisplayIndex(
            @RequestMemberId
            String memberId,

            @ULID
            @Parameter(description = "사진 ID", example = "test_photo_id")
            @PathVariable
            String photoId,

            @Valid
            @RequestBody
            PhotoUpdateDisplayIndexRequest request
    );

    @Operation(summary = "사진 삭제", description = "사진을 삭제합니다.")
    @DeleteMapping("{photoId}")
    Mono<Void> deletePhoto(
            @RequestMemberId
            String memberId,

            @ULID
            @Parameter(description = "사진 ID", example = "test_photo_id")
            @PathVariable
            String photoId
    );
}

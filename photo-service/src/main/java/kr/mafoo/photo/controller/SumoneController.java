package kr.mafoo.photo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.mafoo.photo.annotation.RequestMemberId;
import kr.mafoo.photo.controller.dto.request.ObjectStoragePreSignedUrlRequest;
import kr.mafoo.photo.controller.dto.response.AlbumResponse;
import kr.mafoo.photo.controller.dto.response.PhotoResponse;
import kr.mafoo.photo.controller.dto.response.PreSignedUrlResponse;
import kr.mafoo.photo.controller.dto.response.SumoneBulkUrlRequest;
import kr.mafoo.photo.controller.dto.response.SumoneSummaryResponse;
import kr.mafoo.photo.domain.enums.AlbumType;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Validated
@Tag(name = "썸원 이벤트용 API")
@RestController
@RequestMapping("/v1/sumone")
public class SumoneController {
    @Operation(summary = "통계 api")
    @GetMapping("/summary")
    public SumoneSummaryResponse getSummary() {
        return new SumoneSummaryResponse(1024);
    }

    @Operation(summary = "앨범 생성 api")
    @PostMapping("/albums")
    public Mono<AlbumResponse> createAlbum() {
        return Mono.just(new AlbumResponse("test_album_id", "야뿌들", AlbumType.SUMONE, "6"));
    }

    @Operation(summary = "앨범에 이미지 url 추가")
    @PostMapping("/albums/{albumId}/photos")
    public Flux<PhotoResponse> addPhotos(
            @PathVariable String albumId,
            @RequestBody SumoneBulkUrlRequest request
    ) {
        return Flux.empty();
    }

    @Operation(summary = "앨범에 이미지 조회")
    @GetMapping("/albums/{albumId}/photos")
    public Flux<PhotoResponse> getPhotos(
            @PathVariable String albumId
    ) {
        return Flux.empty();
    }

    @Operation(summary = "Pre-signed Url 요청", description = "Pre-signed Url 목록을 발급합니다.")
    @PostMapping("/albums/{albumId}/presigned-urls")
    Mono<PreSignedUrlResponse> createPreSignedUrls(
            @PathVariable String albumId,
            @RequestBody
            ObjectStoragePreSignedUrlRequest request
    ) {
        return Mono.empty();
    }
}

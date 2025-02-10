package kr.mafoo.photo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.mafoo.photo.annotation.RequestMemberId;
import kr.mafoo.photo.controller.dto.request.ObjectStoragePreSignedUrlRequest;
import kr.mafoo.photo.controller.dto.response.PreSignedUrlResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Validated
@Tag(name = "Object Storage 관련 API", description = "Object Storage 관련 API")
@RequestMapping("/v1/object-storage")
public interface ObjectStorageApi {

    @Operation(summary = "Pre-signed Url 요청", description = "Pre-signed Url 목록을 발급합니다.")
    @PostMapping
    Mono<PreSignedUrlResponse> createPreSignedUrls(
            @RequestMemberId
            String memberId,

            @RequestBody
            ObjectStoragePreSignedUrlRequest request
    );

    @Operation(summary = "리캡 Pre-signed Url 요청", description = "리캡 생성을 위한 Pre-signed Url 목록을 발급합니다.")
    @PostMapping("/recap")
    Mono<PreSignedUrlResponse> createRecapPreSignedUrls(
            @RequestMemberId
            String memberId,

            @RequestBody
            ObjectStoragePreSignedUrlRequest request
    );

}

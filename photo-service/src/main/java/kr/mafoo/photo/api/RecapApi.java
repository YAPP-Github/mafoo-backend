package kr.mafoo.photo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.mafoo.photo.annotation.RequestMemberId;
import kr.mafoo.photo.controller.dto.request.RecapCreateRequest;
import kr.mafoo.photo.controller.dto.request.RecapCreateRequestOld;
import kr.mafoo.photo.controller.dto.response.RecapResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Validated
@Tag(name = "리캡 관련 API", description = "리캡 생성 API")
@RequestMapping("/v1/recaps")
public interface RecapApi {
//    @Operation(summary = "(구 버전) 리캡 생성", description = "앨범의 리캡을 생성합니다.")
//    @PostMapping
//    Mono<RecapResponse> createRecapOriginal(
//        @RequestMemberId
//        String memberId,
//
//        @Valid
//        @RequestBody
//        RecapCreateRequestOld request,
//
//        @Parameter(description = "정렬 종류", example = "ASC | DESC")
//        @RequestParam(required = false)
//        String sort,
//
//        // Authorization Header를 받아올 목적
//        ServerHttpRequest serverHttpRequest
//    );

    @Operation(summary = "리캡 비디오 생성", description = "리캡 영상을 생성합니다.")
    @PostMapping
    Mono<RecapResponse> createRecapVideo(
        @RequestMemberId
        String memberId,

        @Valid
        @RequestBody
        RecapCreateRequest request
    );

}

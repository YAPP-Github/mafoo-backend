package kr.mafoo.photo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.mafoo.photo.annotation.RequestMemberId;
import kr.mafoo.photo.controller.dto.request.RecapCreateRequest;
import kr.mafoo.photo.controller.dto.response.RecapResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Validated
@Tag(name = "리캡 관련 API", description = "리캡 생성 API")
@RequestMapping("/v1/recaps")
public interface RecapApi {
    @Operation(summary = "리캡 생성", description = "앨범의 리캡을 생성합니다.")
    @PostMapping
    Mono<RecapResponse> createRecap(
            @RequestMemberId
            String memberId,

            @Valid
            @RequestBody
            RecapCreateRequest request,

            @Parameter(description = "정렬 종류", example = "ASC | DESC")
            @RequestParam(required = false)
            String sort,

            // Authorization Header를 받아올 목적
            ServerHttpRequest serverHttpRequest
    );

}

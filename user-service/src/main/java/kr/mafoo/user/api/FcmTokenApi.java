package kr.mafoo.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.mafoo.user.annotation.RequestMemberId;
import kr.mafoo.user.annotation.ULID;
import kr.mafoo.user.controller.dto.request.FcmTokenCreateRequest;
import kr.mafoo.user.controller.dto.request.FcmTokenUpdateRequest;
import kr.mafoo.user.controller.dto.response.FcmTokenResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@Tag(name = "FCM 토큰 관련 API", description = "FCM 토큰 조회, 생성, 수정, 삭제 등 API")
@RequestMapping("/v1/fcm-token")
public interface FcmTokenApi {
    @Operation(summary = "토큰 목록 조회")
    @GetMapping
    Flux<FcmTokenResponse> getFcmTokenList(
        @RequestMemberId
        String memberId
    );

    @Operation(summary = "토큰 생성")
    @PostMapping()
    Mono<FcmTokenResponse> createFcmToken(
        @RequestMemberId
        String memberId,

        @Valid
        @RequestBody
        FcmTokenCreateRequest request
    );

    @Operation(summary = "토큰 수정")
    @PutMapping("/{fcmTokenId}")
    Mono<FcmTokenResponse> updateFcmToken(
        @RequestMemberId
        String memberId,

        @ULID
        @Parameter(description = "FCM 토큰 ID", example = "test_fcm_token_id")
        @PathVariable
        String fcmTokenId,

        @Valid
        @RequestBody
        FcmTokenUpdateRequest request
    );

    @Operation(summary = "토큰 삭제")
    @DeleteMapping("/{fcmTokenId}")
    Mono<Void> deleteFcmToken(
        @RequestMemberId
        String memberId,

        @ULID
        @Parameter(description = "FCM 토큰 ID", example = "test_fcm_token_id")
        @PathVariable
        String fcmTokenId
    );
}

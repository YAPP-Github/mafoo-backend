package kr.mafoo.user.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "FCM 토큰 수정 요청")
public record FcmTokenUpdateRequest(
    @Schema(description = "토큰 값", example = "fcm_token")
    String token
) {
}

package kr.mafoo.user.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 갱신 요청")
public record TokenRefreshRequest(
        @Schema(description = "리프레시 토큰", example = "test")
        String refreshToken
) {
}

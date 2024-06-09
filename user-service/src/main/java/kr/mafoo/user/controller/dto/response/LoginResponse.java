package kr.mafoo.user.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답")
public record LoginResponse(
        @Schema(description = "엑세스 토큰", example = "test")
        String accessToken,

        @Schema(description = "리프레시 토큰", example = "test")
        String refreshToken
) {
}

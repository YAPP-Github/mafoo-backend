package kr.mafoo.user.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카카오 로그인 요청")
public record KakaoNativeLoginRequest(
        @Schema(description = "카카오 엑세스 토큰", example = "test")
        String accessToken
) {
}

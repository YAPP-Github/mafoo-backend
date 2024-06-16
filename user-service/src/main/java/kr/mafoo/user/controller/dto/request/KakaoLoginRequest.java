package kr.mafoo.user.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카카오 로그인 요청")
public record KakaoLoginRequest(
        @Schema(description = "인가 코드", example = "test")
        String code
) {
}

package kr.mafoo.user.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "애플 로그인 요청")
public record AppleLoginRequest(
        @Schema(description = "인가 코드", example = "test")
        String code
) {
}

package kr.mafoo.photo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.exception.ErrorCode;

@Schema(description = "에러 응답")
public record ErrorResponse(
        @Schema(description = "에러 코드", example = "ME0001")
        String code,

        @Schema(description = "에러 메시지", example = "사용자를 찾을 수 없습니다")
        String message
) {
        public static ErrorResponse fromErrorCode(ErrorCode errorCode) {
                return new ErrorResponse(
                        errorCode.getCode(),
                        errorCode.getMessage()
                );
        }
}

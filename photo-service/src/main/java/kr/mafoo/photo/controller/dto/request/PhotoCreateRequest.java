package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사진 생성 요청")
public record PhotoCreateRequest(
        @Schema(description = "QR URL", example = "qr_url")
        String qrUrl
) {
}

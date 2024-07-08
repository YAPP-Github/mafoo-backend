package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.URL;

@Schema(description = "사진 생성 요청")
public record PhotoCreateRequest(
        @URL
        @Schema(description = "QR URL", example = "qr_url")
        String qrUrl
) {
}

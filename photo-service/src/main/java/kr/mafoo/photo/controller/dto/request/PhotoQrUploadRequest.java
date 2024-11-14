package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.URL;

@Schema(description = "QR 사진 업로드 요청")
public record PhotoQrUploadRequest(
        @URL
        @Schema(description = "QR URL", example = "qr_url")
        String qrUrl
) {
}

package kr.mafoo.photo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.domain.BrandType;
import kr.mafoo.photo.domain.PhotoEntity;

@Schema(description = "Pre-signed Url 응답")
public record PreSignedUrlResponse(
        @Schema(description = "URL 목록", example = "url")
        String[] urls
) {
        public static PreSignedUrlResponse fromStringArray(
                String[] urls
        ) {
                return new PreSignedUrlResponse(
                        urls
                );
        }
}

package kr.mafoo.photo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Pre-signed Url 응답")
public record PreSignedUrlResponse(
        @Schema(description = "URL 목록", example = "url")
        List<String> urls
) {
        public static PreSignedUrlResponse fromList(
                List<String> urls
        ) {
                return new PreSignedUrlResponse(
                        urls
                );
        }
}

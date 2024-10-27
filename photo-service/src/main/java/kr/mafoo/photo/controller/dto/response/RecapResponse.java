package kr.mafoo.photo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리캡 응답")
public record RecapResponse(

        @Schema(description = "리캡 URL", example = "recap_url")
        String recapUrl

) {
        public static RecapResponse fromString(
                String recapUrl
        ) {
                return new RecapResponse(
                        recapUrl
                );
        }
}

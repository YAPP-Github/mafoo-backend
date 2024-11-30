package kr.mafoo.photo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.service.dto.RecapUrlDto;

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

        public static RecapResponse fromDto(
            RecapUrlDto dto
        ) {
                return new RecapResponse(
                    dto.recapUrl()
                );
        }
}

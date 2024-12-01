package kr.mafoo.recap.controller.dto.response;

public record RecapResponse(
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

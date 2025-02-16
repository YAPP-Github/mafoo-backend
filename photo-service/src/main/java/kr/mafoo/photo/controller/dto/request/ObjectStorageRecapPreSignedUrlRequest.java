package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.annotation.ULID;

@Schema(description = "Pre-signed Url 발급 요청")
public record ObjectStorageRecapPreSignedUrlRequest(
        @Schema(description = "파일 개수", example = "5")
        Integer fileCount,

        @ULID
        @Schema(description = "앨범 ID", example = "test_album_id")
        String albumId
) {
}

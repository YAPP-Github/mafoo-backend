package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "파일(url) 사진 n건 업로드 요청")
public record PhotoFileUrlUploadRequest(
        @ArraySchema(
                schema = @Schema(description = "파일 URL 목록"),
                arraySchema = @Schema(example = "[\"file_url_1\", \"file_url_2\", \"file_url_3\"]")
        )
        String[] fileUrls
) {
}

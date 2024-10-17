package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Pre-signed Url 발급 요청")
public record ObjectStoragePreSignedUrlRequest(
        @ArraySchema(
                schema = @Schema(description = "파일 이름 목록"),
                arraySchema = @Schema(example = "[\"test_file_name_1\", \"test_file_name_2\", \"test_file_name_3\"]")
        )
        String[] fileNames
) {
}

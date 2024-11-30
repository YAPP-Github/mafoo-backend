package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.mafoo.photo.annotation.ULID;

@Schema(description = "리캡 비디오 생성 요청")
public record RecapCreateRequest(
    @ArraySchema(
        schema = @Schema(description = "파일 URL 목록"),
        arraySchema = @Schema(example = "[\"file_url_1\", \"file_url_2\", \"file_url_3\"]")
    )
    List<String> fileUrls,

    @ULID
    @Schema(description = "앨범 ID", example = "test_album_id")
    String albumId
) {
}

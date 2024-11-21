package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.annotation.ULID;

@Schema(description = "사진 n건 앨범 수정 요청")
public record PhotoUpdateBulkAlbumRequest(

        @ArraySchema(
                schema = @Schema(description = "사진 ID 목록"),
                arraySchema = @Schema(example = "[\"test_photo_id_1\", \"test_photo_id_2\", \"test_photo_id_3\"]")
        )
        String[] photoIds,

        @ULID
        @Schema(description = "앨범 ID", example = "test_album_id")
        String albumId
) {
}

package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.annotation.ULID;

@Schema(description = "사진 앨범 수정 요청")
public record PhotoListUpdateAlbumIdRequest(

        @Schema(description = "사진 ID 목록")
        String[] photoIdList,

        @ULID
        @Schema(description = "앨범 ID", example = "test_album_id")
        String albumId
) {
}

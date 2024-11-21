package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.annotation.ULID;

@Schema(description = "사진 앨범 설정 요청")
public record PhotoSetAlbumRequest(
        @ULID
        @Schema(description = "앨범 ID", example = "test_album_id")
        String albumId
) {
}

package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사진 앨범 수정 요청")
public record PhotoAlbumUpdateRequest(
        @Schema(description = "앨범 ID", example = "test_album_id")
        String albumId
) {
}

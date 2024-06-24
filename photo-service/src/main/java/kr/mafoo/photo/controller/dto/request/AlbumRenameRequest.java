package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "앨범 이름 수정 요청")
public record AlbumRenameRequest(
        @Schema(description = "새 앨범 이름", example = "시금치파슷하")
        String name
) {
}

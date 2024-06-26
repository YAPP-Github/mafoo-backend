package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "앨범 생성 요청")
public record AlbumCreateRequest(
        @Schema(description = "앨범 이름", example = "시금치파슷하")
        String name,

        @Schema(description = "앨범 타입", example = "TYPE_A")
        String type
) {
}

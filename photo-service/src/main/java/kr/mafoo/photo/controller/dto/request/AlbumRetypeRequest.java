package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.domain.AlbumType;

@Schema(description = "앨범 타입 수정 요청")
public record AlbumRetypeRequest(
        @Schema(description = "새 앨범 타입")
        AlbumType type
) {
}

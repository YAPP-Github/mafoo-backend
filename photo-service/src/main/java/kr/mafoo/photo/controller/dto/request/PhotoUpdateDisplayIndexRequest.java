package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "사진 인덱스 수정 요청")
public record PhotoUpdateDisplayIndexRequest(
        @NotNull
        @Schema(description = "사진 표시 인덱스", example = "photo_display_index")
        Integer newDisplayIndex
) {
}

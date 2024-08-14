package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "앨범 수정 요청")
public record AlbumUpdateDisplayIndexRequest(
        @NotNull
        @Min(0)
        @Schema(description = "대상 인덱스", example = "1")
        Long newDisplayIndex
) {
}

package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kr.mafoo.photo.annotation.MatchEnum;
import kr.mafoo.photo.domain.AlbumType;
import org.hibernate.validator.constraints.Length;

@Schema(description = "앨범 수정 요청")
public record AlbumUpdateRequest(
        @NotBlank
        @Length(min = 1, max = 100)
        @Schema(description = "앨범 이름", example = "시금치파슷하")
        String name,

        @MatchEnum(enumClass = AlbumType.class)
        @Schema(description = "앨범 타입")
        String type
) {
}

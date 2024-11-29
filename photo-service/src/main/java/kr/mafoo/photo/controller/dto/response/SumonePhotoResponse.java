package kr.mafoo.photo.controller.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.domain.PhotoEntity;

@Schema(description = "앨범 응답")
public record SumonePhotoResponse(
        @Schema(description = "사진 URL", example = "photo_url")
        String photoUrl
) {
    public static SumonePhotoResponse fromEntity(PhotoEntity entity) {
        return new SumonePhotoResponse(
                entity.getPhotoUrl()
        );
    }
}

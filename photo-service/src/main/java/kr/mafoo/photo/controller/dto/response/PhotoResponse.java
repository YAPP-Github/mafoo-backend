package kr.mafoo.photo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.domain.enums.BrandType;
import kr.mafoo.photo.domain.PhotoEntity;

@Schema(description = "사진 응답")
public record PhotoResponse(
        @Schema(description = "사진 ID", example = "test_photo_id")
        String photoId,

        @Schema(description = "사진 URL", example = "photo_url")
        String photoUrl,

        @Schema(description = "사진 브랜드", example = "LIFE_FOUR_CUTS")
        BrandType brand,

        @Schema(description = "앨범 ID", example = "test_album_id")
        String albumId,

        @Schema(description = "앨범 생성일")
        String createdAt
) {
        public static PhotoResponse fromEntity(
                PhotoEntity entity
        ) {
                return new PhotoResponse(
                        entity.getPhotoId(),
                        entity.getPhotoUrl(),
                        entity.getBrand(),
                        entity.getAlbumId(),
                        entity.getCreatedAt().toString()
                );
        }
}

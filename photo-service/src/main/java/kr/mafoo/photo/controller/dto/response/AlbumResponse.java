package kr.mafoo.photo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.enums.AlbumType;

@Schema(description = "앨범 응답")
public record AlbumResponse(
        @Schema(description = "앨범 ID", example = "test_album_id")
        String albumId,

        @Schema(description = "앨범 이름", example = "야뿌들")
        String name,

        @Schema(description = "앨범 종류", example = "TYPE_B")
        AlbumType type,

        @Schema(description = "앨범 내 사진 수", example = "6")
        String photoCount
) {
        public static AlbumResponse fromEntity(AlbumEntity entity) {
                return new AlbumResponse(
                        entity.getAlbumId(),
                        entity.getName(),
                        entity.getType(),
                        entity.getPhotoCount().toString()
                );
        }
}

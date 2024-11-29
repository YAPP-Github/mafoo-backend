package kr.mafoo.photo.controller.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.domain.AlbumEntity;

@Schema(description = "앨범 응답")
public record SumoneAlbumResponse(
        @Schema(description = "앨범 ID", example = "test_album_id")
        String albumId
) {
    public static SumoneAlbumResponse fromEntity(AlbumEntity entity) {
        return new SumoneAlbumResponse(
                entity.getAlbumId()
        );
    }
}

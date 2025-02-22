package kr.mafoo.photo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.domain.AlbumExportEntity;

@Schema(description = "앨범 내보내기 응답")
public record AlbumExportResponse(
        String exportId,
        String albumId
) {
    public static AlbumExportResponse fromEntity(AlbumExportEntity entity) {
        return new AlbumExportResponse(entity.getExportId(), entity.getAlbumId());
    }
}

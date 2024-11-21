package kr.mafoo.photo.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.enums.AlbumType;

import java.time.LocalDateTime;

@Schema(description = "앨범 응답")
public record AlbumRawResponse(
        @Schema(description = "앨범 ID", example = "test_album_id")
        String albumId,

        @Schema(description = "앨범 이름", example = "야뿌들")
        String name,

        @Schema(description = "앨범 종류", example = "TYPE_B")
        AlbumType type,

        @Schema(description = "앨범 내 사진 수", example = "6")
        String photoCount,

        @Schema(description = "앨범 주인 ID", example = "6")
        String ownerMemberId,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        @Schema(description = "생성 시간", example = "2021-08-01 00:00:00")
        LocalDateTime createdAt
) {
    public static AlbumRawResponse fromEntity(AlbumEntity entity) {
        return new AlbumRawResponse(
                entity.getAlbumId(),
                entity.getName(),
                entity.getType(),
                entity.getPhotoCount().toString(),
                entity.getOwnerMemberId(),
                entity.getCreatedAt()
        );
    }
}

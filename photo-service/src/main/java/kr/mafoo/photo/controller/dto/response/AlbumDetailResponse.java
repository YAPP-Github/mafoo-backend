package kr.mafoo.photo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.domain.enums.AlbumType;
import kr.mafoo.photo.domain.enums.PermissionLevel;
import kr.mafoo.photo.domain.enums.ShareStatus;
import kr.mafoo.photo.service.dto.AlbumDto;

@Schema(description = "앨범 응답")
public record AlbumDetailResponse(
        @Schema(description = "앨범 ID", example = "test_album_id")
        String albumId,

        @Schema(description = "앨범 이름", example = "야뿌들")
        String name,

        @Schema(description = "앨범 종류", example = "HEART")
        AlbumType type,

        @Schema(description = "앨범 내 사진 수", example = "6")
        String photoCount,

        @Schema(description = "공유 받은 앨범 여부", example = "false")
        Boolean isShared,

        @Schema(description = "공유 받은 앨범 상태", example = "null")
        ShareStatus shareStatus,

        @Schema(description = "공유 받은 앨범 권한", example = "null")
        PermissionLevel permissionLevel,

        @Schema(description = "공유 받은 앨범 소유자 프로필 사진", example = "null")
        String ownerProfileImgUrl
) {
        public static AlbumDetailResponse fromDto(
            AlbumDto dto
        ) {
                return new AlbumDetailResponse(
                    dto.albumId(),
                    dto.name(),
                    dto.type(),
                    dto.photoCount().toString(),
                    dto.isShared(),
                    dto.shareStatus(),
                    dto.permissionLevel(),
                    dto.ownerProfileImgUrl()
                );
        }
}

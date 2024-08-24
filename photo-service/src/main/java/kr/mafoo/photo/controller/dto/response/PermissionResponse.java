package kr.mafoo.photo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.domain.PermissionEntity;
import kr.mafoo.photo.domain.PermissionType;

@Schema(description = "권한 응답")
public record PermissionResponse(
        @Schema(description = "권한 ID", example = "test_permission_id")
        String permissionId,

        @Schema(description = "권한 종류", example = "FULL_ACCESS")
        PermissionType type,

        @Schema(description = "권한 대상 사용자", example = "test_member_id")
        String memberId,

        @Schema(description = "권한 대상 앨범", example = "test_album_id")
        String albumId
) {
        public static PermissionResponse fromEntity(
                PermissionEntity entity
        ) {
                return new PermissionResponse(
                        entity.getPermissionId(),
                        entity.getType(),
                        entity.getMemberId(),
                        entity.getAlbumId()
                );
        }
}

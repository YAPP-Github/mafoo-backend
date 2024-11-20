package kr.mafoo.photo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.domain.SharedMemberEntity;
import kr.mafoo.photo.domain.enums.PermissionLevel;
import kr.mafoo.photo.domain.enums.ShareStatus;

@Schema(description = "공유 사용자 응답")
public record SharedMemberResponse(
        @Schema(description = "공유 사용자 ID", example = "test_shared_member_id")
        String sharedMemberId,

        @Schema(description = "공유 상태", example = "PENDING")
        ShareStatus shareStatus,

        @Schema(description = "권한 단계", example = "FULL_ACCESS")
        PermissionLevel permissionLevel,

        @Schema(description = "공유 대상 앨범", example = "test_album_id")
        String albumId,

        @Schema(description = "공유 대상 사용자", example = "test_album_id")
        String memberId
) {
        public static SharedMemberResponse fromEntity(
            SharedMemberEntity entity
        ) {
            return new SharedMemberResponse(
                entity.getSharedMemberId(),
                entity.getShareStatus(),
                entity.getPermissionLevel(),
                entity.getAlbumId(),
                entity.getMemberId()
            );
        }
}

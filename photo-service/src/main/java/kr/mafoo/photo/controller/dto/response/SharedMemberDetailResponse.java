package kr.mafoo.photo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.domain.enums.PermissionLevel;
import kr.mafoo.photo.domain.enums.ShareStatus;
import kr.mafoo.photo.service.dto.SharedMemberDetailDto;

@Schema(description = "공유 사용자 응답")
public record SharedMemberDetailResponse(
        @Schema(description = "공유 사용자 ID", example = "test_shared_member_id")
        String sharedMemberId,

        @Schema(description = "공유 상태", example = "PENDING")
        ShareStatus shareStatus,

        @Schema(description = "권한 단계", example = "FULL_ACCESS")
        PermissionLevel permissionLevel,

        @Schema(description = "공유 대상 앨범 ID", example = "test_album_id")
        String albumId,

        @Schema(description = "공유 대상 사용자 ID", example = "test_member_id")
        String memberId,

        @Schema(description = "공유 대상 사용자 프로필 사진 URL", example = "test_member_profile_url")
        String profileImageUrl,

        @Schema(description = "공유 대상 사용자 이름", example = "test_member_name")
        String memberName
) {
        public static SharedMemberDetailResponse fromDto(
            SharedMemberDetailDto dto
        ) {
            return new SharedMemberDetailResponse(
                dto.sharedMemberId(),
                dto.shareStatus(),
                dto.permissionLevel(),
                dto.albumId(),
                dto.memberId(),
                dto.profileImageUrl(),
                dto.memberName()
            );
        }
}

package kr.mafoo.photo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.domain.enums.PermissionLevel;
import kr.mafoo.photo.domain.enums.ShareStatus;
import kr.mafoo.photo.service.dto.SharedMemberDto;

@Schema(description = "사용자 응답")
public record SharedMemberDetailResponse(
        @Schema(description = "공유 사용자 ID", example = "test_shared_member_id")
        String sharedMemberId,

        @Schema(description = "공유 상태", example = "PENDING")
        ShareStatus shareStatus,

        @Schema(description = "권한 단계", example = "FULL_ACCESS")
        PermissionLevel permissionLevel,

        @Schema(description = "공유 대상 사용자 ID", example = "test_member_id")
        String memberId,

        @Schema(description = "사용자 이름", example = "시금치파슷하")
        String name,

        @Schema(description = "프로필 이미지 URL", example = "https://mafoo.kr/profile.jpg")
        String profileImageUrl,

        @Schema(description = "식별 번호", example = "0000")
        String serialNumber
) {
        public static SharedMemberDetailResponse fromDto(
            SharedMemberDto dto
        ) {
            return new SharedMemberDetailResponse(
                dto.sharedMemberId(),
                dto.shareStatus(),
                dto.permissionLevel(),
                dto.memberId(),
                dto.profileImageUrl(),
                dto.name(),
                dto.serialNumber()
            );
        }
}

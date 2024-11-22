package kr.mafoo.user.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.user.service.dto.MemberDetailDto;

@Schema(description = "사용자의 정보 응답")
public record MemberDetailResponse(
        @Schema(description = "사용자 ID", example = "test")
        String memberId,

        @Schema(description = "사용자 이름", example = "송영민")
        String name,

        @Schema(description = "프로필 이미지 URL", example = "https://mafoo.kr/profile.jpg")
        String profileImageUrl,

        @Schema(description = "식별 번호", example = "0000")
        String serialNumber,

        @Schema(description = "공유 사용자 ID", example = "test_shared_member_id")
        String sharedMemberId,

        @Schema(description = "공유 상태", example = "PENDING")
        String shareStatus,

        @Schema(description = "권한 단계", example = "FULL_ACCESS")
        String permissionLevel
) {
        public static MemberDetailResponse fromDto(
            MemberDetailDto dto
        ) {
                return new MemberDetailResponse(
                    dto.memberId(),
                    dto.name(),
                    dto.profileImageUrl(),
                    dto.serialNumber(),
                    dto.sharedMemberId(),
                    dto.shareStatus(),
                    dto.permissionLevel()
                );
        }
}

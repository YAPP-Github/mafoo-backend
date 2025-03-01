package kr.mafoo.user.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.user.service.dto.MeDto;

@Schema(description = "사용자 정보 상세 응답")
public record MeResponse(
        @Schema(description = "사용자 ID", example = "test")
        String memberId,

        @Schema(description = "사용자 이름", example = "송영민")
        String name,

        @Schema(description = "닉네임 기본값 여부", example = "false")
        boolean isDefaultName,

        @Schema(description = "프로필 이미지 URL", example = "https://mafoo.kr/profile.jpg")
        String profileImageUrl,

        @Schema(description = "식별 번호", example = "0000")
        String serialNumber,

        String fcmToken
) {
        public static MeResponse fromDto(
            MeDto dto
        ) {
                return new MeResponse(
                    dto.memberId(),
                    dto.name(),
                    dto.isDefaultName(),
                    dto.profileImageUrl(),
                    dto.serialNumber(),
                    dto.fcmToken()
                );
        }
}

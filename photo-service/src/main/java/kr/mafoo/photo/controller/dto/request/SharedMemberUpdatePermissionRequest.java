package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.annotation.MatchEnum;
import kr.mafoo.photo.domain.enums.PermissionLevel;

@Schema(description = "공유 사용자 권한 변경 요청")
public record SharedMemberUpdatePermissionRequest(
        @MatchEnum(enumClass = PermissionLevel.class)
        @Schema(description = "권한 단계", example = "FULL_ACCESS")
        String permissionLevel
) {
}

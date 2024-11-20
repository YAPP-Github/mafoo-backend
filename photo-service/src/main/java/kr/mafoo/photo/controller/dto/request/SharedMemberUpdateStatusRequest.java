package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.annotation.MatchEnum;
import kr.mafoo.photo.domain.enums.ShareStatus;

@Schema(description = "공유 사용자 상태 수정 요청")
public record SharedMemberUpdateStatusRequest(
        @MatchEnum(enumClass = ShareStatus.class)
        @Schema(description = "공유 상태", example = "PENDING")
        String shareStatus
) {
}

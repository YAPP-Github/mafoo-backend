package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.annotation.MatchEnum;
import kr.mafoo.photo.domain.PermissionType;

@Schema(description = "권한 종류 수정 요청")
public record PermissionTypeUpdateRequest(
        @MatchEnum(enumClass = PermissionType.class)
        @Schema(description = "권한 종류", example = "FULL_ACCESS")
        String type
) {
}

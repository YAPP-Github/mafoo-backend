package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.annotation.MatchEnum;
import kr.mafoo.photo.annotation.ULID;
import kr.mafoo.photo.domain.PermissionType;

@Schema(description = "권한 n건 생성 요청")
public record PermissionCreateRequest(

        @ULID
        @Schema(description = "권한 대상 사용자 아이디", example = "test_member_id")
        String memberId,

        @MatchEnum(enumClass = PermissionType.class)
        @Schema(description = "권한 종류", example = "FULL_ACCESS")
        String type
) {
}

package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.annotation.MatchEnum;
import kr.mafoo.photo.annotation.ULID;
import kr.mafoo.photo.domain.enums.PermissionLevel;

@Schema(description = "공유 사용자 생성 요청")
public record SharedMemberCreateRequest(

    @Schema(description = "공유 대상 앨범 ID", example = "test_album_id")
    String albumId,

    @ULID
    @Schema(description = "공유 대상 사용자 ID", example = "test_member_id")
    String memberId,

    @MatchEnum(enumClass = PermissionLevel.class)
    @Schema(description = "권한 단계", example = "FULL_ACCESS")
    String permissionLevel
) {
}

package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.annotation.ULID;

@Schema(description = "앨범 소유자 변경 요청")
public record AlbumUpdateOwnershipRequest(
        @ULID
        @Schema(description = "사용자 ID", example = "test_member_id")
        String newOwnerMemberId
) {
}

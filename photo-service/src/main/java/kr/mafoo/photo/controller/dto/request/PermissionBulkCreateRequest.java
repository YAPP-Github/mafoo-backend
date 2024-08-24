package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.annotation.ULID;

import java.util.List;

@Schema(description = "권한 n건 생성 요청")
public record PermissionBulkCreateRequest(

        List<PermissionCreateRequest> permissions,

        @ULID
        @Schema(description = "앨범 ID", example = "test_album_id")
        String albumId
) {
}

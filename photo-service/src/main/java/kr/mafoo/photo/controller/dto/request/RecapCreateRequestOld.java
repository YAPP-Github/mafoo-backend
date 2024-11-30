package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.annotation.ULID;

@Schema(description = "리캡 생성 요청")
public record RecapCreateRequestOld(
        @ULID
        @Schema(description = "앨범 ID", example = "test_album_id")
        String albumId
) {
}

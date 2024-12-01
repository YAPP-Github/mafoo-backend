package kr.mafoo.photo.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "썸원 앨범 생성 요청")
public record SumoneAlbumCreateRequest(
    @Schema(description = "썸원 유저ID")
    String userId
) {
}

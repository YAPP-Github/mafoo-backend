package kr.mafoo.photo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리캡 응답")
public record SumoneSummaryResponse(
        @Schema(description = "사용자 수", example = "1565")
        int userCount
){

}

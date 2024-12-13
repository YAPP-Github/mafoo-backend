package kr.mafoo.user.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이름 변경 요청")
public record ChangeNameRequest(
        @Schema(description = "새 이름", example = "염수연")
        String name
) {
}

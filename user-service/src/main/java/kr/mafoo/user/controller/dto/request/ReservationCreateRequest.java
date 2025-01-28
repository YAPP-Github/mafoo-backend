package kr.mafoo.user.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import kr.mafoo.user.enums.ReservationStatus;
import kr.mafoo.user.enums.VariableDomain;
import kr.mafoo.user.enums.VariableSort;
import kr.mafoo.user.enums.VariableType;

@Schema(description = "예약 생성 요청")
public record ReservationCreateRequest(
    @Schema(description = "템플릿 ID", example = "test_template_id")
    String templateId,

    @Schema(description = "예약 상태(ACTIVE, INACTIVE, SENT)", example = "ACTIVE")
    ReservationStatus status,

    @Schema(description = "변수 도메인", example = "")
    VariableDomain variableDomain,

    @Schema(description = "변수 상태", example = "")
    VariableType variableType,

    @Schema(description = "변수 정렬", example = "")
    VariableSort variableSort,

    @ArraySchema(
        schema = @Schema(description = "전송 사용자 ID 목록"),
        arraySchema = @Schema(example = "[\"test_member_id_1\", \"test_member_id_2\", \"test_member_id_3\"]")
    )
    List<String> receiverMemberIds,

    @Schema(description = "전송 일시", example = "2025-01-01 00:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime sendAt,

    @Schema(description = "전송 반복 주기", example = "7")
    Integer sendRepeatInterval
) {
}

package kr.mafoo.user.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import kr.mafoo.user.domain.ReservationEntity;
import kr.mafoo.user.enums.ReservationStatus;
import kr.mafoo.user.enums.VariableDomain;
import kr.mafoo.user.enums.VariableSort;
import kr.mafoo.user.enums.VariableType;
import kr.mafoo.user.service.dto.ReservationDto;

@Schema(description = "예약 응답")
public record ReservationResponse(
    @Schema(description = "예약 ID", example = "test_reservation_id")
    String reservationId,

    @Schema(description = "템플릿 ID", example = "test_template_id")
    String templateId,

    @Schema(description = "예약 상태(ACTIVE, INACTIVE, SENT)", example = "ACTIVE")
    ReservationStatus status,

    @Schema(description = "변수 도메인")
    VariableDomain variableDomain,

    @Schema(description = "변수 종류")
    VariableType variableType,

    @Schema(description = "변수 정렬")
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
    Integer sendRepeatInterval,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "예약 생성 일시")
    LocalDateTime createdAt,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "예약 수정 일시")
    LocalDateTime updatedAt
) {
        public static ReservationResponse fromEntity(
            ReservationEntity entity
        ) {
                return new ReservationResponse(
                    entity.getReservationId(),
                    entity.getTemplateId(),
                    entity.getStatus(),
                    entity.getVariableDomain(),
                    entity.getVariableType(),
                    entity.getVariableSort(),
                    entity.getReceiverMemberIds().lines().toList(),
                    entity.getSendAt(),
                    entity.getSendRepeatInterval(),
                    entity.getCreatedAt(),
                    entity.getUpdatedAt()
                );
        }

        public static ReservationResponse fromDto(
            ReservationDto dto
        ) {
                return new ReservationResponse(
                    dto.reservationId(),
                    dto.templateId(),
                    dto.status(),
                    dto.variableDomain(),
                    dto.variableType(),
                    dto.variableSort(),
                    dto.receiverMemberIds().lines().toList(),
                    dto.sendAt(),
                    dto.sendRepeatInterval(),
                    dto.createdAt(),
                    dto.updatedAt()
                );
        }
}

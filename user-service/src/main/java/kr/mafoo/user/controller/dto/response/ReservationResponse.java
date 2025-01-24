package kr.mafoo.user.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import kr.mafoo.user.domain.ReservationEntity;
import kr.mafoo.user.enums.ReservationStatus;
import kr.mafoo.user.enums.VariableDomain;
import kr.mafoo.user.enums.VariableSort;
import kr.mafoo.user.enums.VariableType;

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

    @Schema(description = "전송 사용자 ID 목록", example = "test_receiver_member_id")
    String receiverMemberIds,

    @Schema(description = "전송 일시")
    LocalDateTime sendAt,

    @Schema(description = "전송 반복 주기", example = "7")
    Integer sendRepeatInterval,

    @Schema(description = "예약 생성 일시")
    LocalDateTime createdAt,

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
                    entity.getReceiverMemberIds(),
                    entity.getSendAt(),
                    entity.getSendRepeatInterval(),
                    entity.getCreatedAt(),
                    entity.getUpdatedAt()
                );
        }
}

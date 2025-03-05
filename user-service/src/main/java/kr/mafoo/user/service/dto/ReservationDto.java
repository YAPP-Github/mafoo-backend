package kr.mafoo.user.service.dto;

import java.time.LocalDateTime;
import kr.mafoo.user.domain.ReservationEntity;
import kr.mafoo.user.enums.ReservationStatus;
import kr.mafoo.user.enums.VariableDomain;
import kr.mafoo.user.enums.VariableSort;
import kr.mafoo.user.enums.VariableParam;

public record ReservationDto(
    String reservationId,
    String templateId,
    ReservationStatus status,
    VariableDomain variableDomain,
    VariableParam variableParam,
    VariableSort variableSort,
    String receiverMemberIds,
    LocalDateTime sendAt,
    Integer sendRepeatInterval,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
        public static ReservationDto fromEntity(
            ReservationEntity entity
        ) {
                return new ReservationDto(
                    entity.getReservationId(),
                    entity.getTemplateId(),
                    entity.getStatus(),
                    entity.getVariableDomain(),
                    entity.getVariableParam(),
                    entity.getVariableSort(),
                    entity.getReceiverMemberIds(),
                    entity.getSendAt(),
                    entity.getSendRepeatInterval(),
                    entity.getCreatedAt(),
                    entity.getUpdatedAt()
                );
        }
}

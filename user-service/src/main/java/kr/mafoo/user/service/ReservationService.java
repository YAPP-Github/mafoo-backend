package kr.mafoo.user.service;

import java.time.LocalDateTime;
import java.util.List;
import kr.mafoo.user.domain.ReservationEntity;
import kr.mafoo.user.enums.ReservationStatus;
import kr.mafoo.user.enums.VariableDomain;
import kr.mafoo.user.enums.VariableSort;
import kr.mafoo.user.enums.VariableParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationQuery reservationQuery;
    private final ReservationCommand reservationCommand;

    @Transactional
    public Mono<ReservationEntity> addReservation(
        String templateId,
        ReservationStatus status,
        VariableDomain variableDomain,
        VariableParam variableParam,
        VariableSort variableSort,
        List<String> receiverMemberIds,
        LocalDateTime sendAt,
        Integer sendRepeatInterval
    ) {
        return reservationQuery.checkDuplicateExists(templateId, sendAt)
            .then(
                reservationCommand.addReservation(templateId, status, variableDomain, variableParam, variableSort,
                    receiverMemberIds.toString(), sendAt, sendRepeatInterval));
    }

    @Transactional
    public Mono<ReservationEntity> modifyReservation(
        String reservationId,
        String templateId,
        ReservationStatus status,
        VariableDomain variableDomain,
        VariableParam variableParam,
        VariableSort variableSort,
        List<String> receiverMemberIds,
        LocalDateTime sendAt,
        Integer sendRepeatInterval
    ) {
        return reservationQuery.findById(reservationId)
            .flatMap(
                reservation -> reservationCommand.modifyReservation(reservation, templateId, status,
                    variableDomain, variableParam, variableSort, receiverMemberIds.toString(), sendAt,
                    sendRepeatInterval));
    }

    @Transactional
    public Mono<Void> removeReservation(
        String reservationId
    ) {
        return reservationCommand.removeReservation(reservationId);
    }
}

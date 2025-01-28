package kr.mafoo.user.service;

import java.time.LocalDateTime;
import kr.mafoo.user.domain.ReservationEntity;
import kr.mafoo.user.enums.ReservationStatus;
import kr.mafoo.user.enums.VariableDomain;
import kr.mafoo.user.enums.VariableSort;
import kr.mafoo.user.enums.VariableType;
import kr.mafoo.user.repository.ReservationRepository;
import kr.mafoo.user.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReservationCommand {

    private final ReservationRepository reservationRepository;

    public Mono<ReservationEntity> addReservation(String templateId, ReservationStatus status, VariableDomain variableDomain, VariableType variableType, VariableSort variableSort, String receiverMemberIds, LocalDateTime sendAt, Integer repeatInterval) {
        return reservationRepository.save(
            ReservationEntity.newReservation(IdGenerator.generate(), templateId, status,
                variableDomain, variableType, variableSort, receiverMemberIds, sendAt, repeatInterval)
        );
    }

    public Mono<ReservationEntity> modifyReservation(
        ReservationEntity reservation, String templateId, ReservationStatus status, VariableDomain variableDomain, VariableType variableType, VariableSort variableSort, String receiverMemberIds, LocalDateTime sendAt, Integer repeatInterval) {
        return reservationRepository.save(
            reservation.updateReservation(templateId, status, variableDomain, variableType, variableSort, receiverMemberIds, sendAt, repeatInterval)
        );
    }

    public Mono<Void> removeReservation(ReservationEntity reservation) {
        return reservationRepository.delete(reservation);
    }
}

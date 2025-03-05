package kr.mafoo.user.service;

import static kr.mafoo.user.enums.ReservationStatus.SENT;

import java.time.LocalDateTime;
import java.util.List;
import kr.mafoo.user.domain.ReservationEntity;
import kr.mafoo.user.enums.ReservationStatus;
import kr.mafoo.user.enums.VariableDomain;
import kr.mafoo.user.enums.VariableSort;
import kr.mafoo.user.enums.VariableParam;
import kr.mafoo.user.repository.ReservationRepository;
import kr.mafoo.user.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReservationCommand {

    private final ReservationRepository reservationRepository;

    public Mono<ReservationEntity> addReservation(String templateId, ReservationStatus status, VariableDomain variableDomain, VariableParam variableParam, VariableSort variableSort, String receiverMemberIds, LocalDateTime sendAt, Integer repeatInterval) {
        return reservationRepository.save(
            ReservationEntity.newReservation(IdGenerator.generate(), templateId, status,
                variableDomain, variableParam, variableSort, receiverMemberIds, sendAt, repeatInterval)
        );
    }

    public Flux<ReservationEntity> modifyReservationAfterSent(List<ReservationEntity> reservations) {
        return Flux.fromIterable(reservations)
            .flatMap(reservation -> {
                Integer sendRepeatInterval = reservation.getSendRepeatInterval();

                if (sendRepeatInterval == null) {
                    return this.modifyReservationStatus(reservation, SENT);
                } else {
                    return this.modifyReservationSendAtWithRepeatInterval(
                        reservation,
                        reservation.getSendAt().plusDays(sendRepeatInterval)
                    );
                }
            });
    }

    private Mono<ReservationEntity> modifyReservationStatus(ReservationEntity reservation, ReservationStatus status) {
        return reservationRepository.save(
            reservation.updateStatus(status)
        );
    }

    private Mono<ReservationEntity> modifyReservationSendAtWithRepeatInterval(ReservationEntity reservation, LocalDateTime sendAt) {
        return reservationRepository.save(
            reservation.updateSendAt(sendAt)
        );
    }

    public Mono<ReservationEntity> modifyReservation(ReservationEntity reservation, String templateId, ReservationStatus status, VariableDomain variableDomain, VariableParam variableParam, VariableSort variableSort, String receiverMemberIds, LocalDateTime sendAt, Integer repeatInterval) {
        return reservationRepository.save(
            reservation.updateReservation(templateId, status, variableDomain, variableParam, variableSort, receiverMemberIds, sendAt, repeatInterval)
        );
    }

    public Mono<Void> removeReservation(String reservationId) {
        return reservationRepository.softDeleteById(reservationId);
    }
}

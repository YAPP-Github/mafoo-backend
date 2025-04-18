package kr.mafoo.user.service;

import java.time.LocalDateTime;
import kr.mafoo.user.domain.ReservationEntity;
import kr.mafoo.user.enums.ReservationStatus;
import kr.mafoo.user.exception.ReservationDuplicatedException;
import kr.mafoo.user.exception.ReservationNotFoundException;
import kr.mafoo.user.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReservationQuery {

    private final ReservationRepository reservationRepository;

    public Mono<ReservationEntity> findById(String reservationId) {
        return reservationRepository.findByReservationIdAndDeletedAtNull(reservationId)
            .switchIfEmpty(Mono.error(new ReservationNotFoundException()));
    }

    public Flux<ReservationEntity> findAllByStatusAndSendAtBefore(ReservationStatus status, LocalDateTime sendAt) {
        return reservationRepository.findAllByStatusAndSendAtBeforeAndDeletedAtNull(status, sendAt)
            .switchIfEmpty(Mono.error(new ReservationNotFoundException()));
    }

    public Flux<ReservationEntity> findByTemplateId(String templateId) {
        return reservationRepository.findAllByTemplateIdAndDeletedAtNull(templateId)
            .switchIfEmpty(Mono.error(new ReservationNotFoundException()));
    }

    public Mono<Void> checkDuplicateExists(String templateId, LocalDateTime sendAt) {
        return reservationRepository.findByTemplateIdAndSendAtAndDeletedAtNull(templateId, sendAt)
            .switchIfEmpty(Mono.empty())
            .flatMap(reservation -> Mono.error(new ReservationDuplicatedException()));
    }
}

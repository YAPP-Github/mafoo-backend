package kr.mafoo.user.service;

import java.time.LocalDateTime;
import kr.mafoo.user.domain.ReservationEntity;
import kr.mafoo.user.exception.ReservationDuplicatedException;
import kr.mafoo.user.exception.ReservationNotFoundException;
import kr.mafoo.user.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReservationQuery {

    private final ReservationRepository reservationRepository;

    public Mono<ReservationEntity> findById(String reservationId) {
        return reservationRepository.findById(reservationId)
            .switchIfEmpty(Mono.error(new ReservationNotFoundException()));
    }

    public Mono<Void> checkDuplicateExists(String templateId, LocalDateTime sendAt) {
        return reservationRepository.findByTemplateIdAndSendAt(templateId, sendAt)
            .switchIfEmpty(Mono.empty())
            .flatMap(reservation -> Mono.error(new ReservationDuplicatedException()));
    }
}

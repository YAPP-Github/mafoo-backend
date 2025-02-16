package kr.mafoo.user.repository;

import java.time.LocalDateTime;
import kr.mafoo.user.domain.ReservationEntity;
import kr.mafoo.user.enums.ReservationStatus;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReservationRepository extends R2dbcRepository<ReservationEntity, String> {
    Flux<ReservationEntity> findAllByStatusAndSendAtBeforeAndDeletedAtNull(ReservationStatus status, LocalDateTime sendAt);
    Flux<ReservationEntity> findAllByTemplateIdAndDeletedAtNull(String templateId);

    Mono<ReservationEntity> findByReservationIdAndDeletedAtNull(String reservationId);
    Mono<ReservationEntity> findByTemplateIdAndSendAtAndDeletedAtNull(String templateId, LocalDateTime sendAt);

    @Modifying
    @Query("UPDATE notification_reservation SET deleted_at = NOW() WHERE notification_reservation_id = :reservationId AND deleted_at IS NULL")
    Mono<Void> softDeleteById(String reservationId);
}

package kr.mafoo.user.repository;

import java.time.LocalDateTime;
import kr.mafoo.user.domain.ReservationEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReservationRepository extends R2dbcRepository<ReservationEntity, String> {
    Flux<ReservationEntity> findByTemplateId(String templateId);
    Mono<ReservationEntity> findByTemplateIdAndSendAt(String templateId, LocalDateTime sendAt);
}

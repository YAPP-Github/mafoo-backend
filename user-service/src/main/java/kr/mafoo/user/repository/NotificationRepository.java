package kr.mafoo.user.repository;

import kr.mafoo.user.domain.NotificationEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface NotificationRepository extends R2dbcRepository<NotificationEntity, String> {
    Flux<NotificationEntity> findAllByReceiverMemberId(String receiverMemberId);
}

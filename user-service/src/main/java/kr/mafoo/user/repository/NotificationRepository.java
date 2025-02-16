package kr.mafoo.user.repository;

import kr.mafoo.user.domain.NotificationEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificationRepository extends R2dbcRepository<NotificationEntity, String> {
    Flux<NotificationEntity> findAllByReceiverMemberIdAndDeletedAtNull(String receiverMemberId);

    Mono<NotificationEntity> findByNotificationIdAndDeletedAtNull(String notificationId);

    @Modifying
    @Query("UPDATE notification SET deleted_at = NOW() WHERE notification_id = :notificationId AND deleted_at IS NULL")
    Mono<Void> softDeleteById(String notificationId);
}

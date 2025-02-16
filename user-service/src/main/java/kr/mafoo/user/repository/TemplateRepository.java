package kr.mafoo.user.repository;

import kr.mafoo.user.domain.TemplateEntity;
import kr.mafoo.user.enums.NotificationType;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TemplateRepository extends R2dbcRepository<TemplateEntity, String> {
    Flux<TemplateEntity> findAllByDeletedAtNull();

    Mono<TemplateEntity> findByTemplateIdAndDeletedAtNull(String templateId);
    Mono<TemplateEntity> findByNotificationTypeAndDeletedAtNull(NotificationType notificationType);

    @Modifying
    @Query("UPDATE notification_template SET deleted_at = NOW() WHERE notification_template_id = :templateId AND deleted_at IS NULL")
    Mono<Void> softDeleteById(String templateId);
}

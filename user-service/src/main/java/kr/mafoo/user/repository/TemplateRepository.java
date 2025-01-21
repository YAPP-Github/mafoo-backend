package kr.mafoo.user.repository;

import kr.mafoo.user.domain.TemplateEntity;
import kr.mafoo.user.enums.NotificationType;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface TemplateRepository extends R2dbcRepository<TemplateEntity, String> {
    Mono<TemplateEntity> findByNotificationType(NotificationType notificationType);
}

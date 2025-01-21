package kr.mafoo.user.service;

import kr.mafoo.user.domain.TemplateEntity;
import kr.mafoo.user.enums.NotificationType;
import kr.mafoo.user.exception.TemplateNotFoundException;
import kr.mafoo.user.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TemplateQuery {

    private final TemplateRepository templateRepository;

    public Flux<TemplateEntity> findAll() {
        return templateRepository.findAll()
            .switchIfEmpty(Mono.error(new TemplateNotFoundException()));
    }

    public Mono<TemplateEntity> findById(String templateId) {
        return templateRepository.findById(templateId)
            .switchIfEmpty(Mono.error(new TemplateNotFoundException()));
    }

    public Mono<TemplateEntity> findByNotificationType(NotificationType notificationType) {
        return templateRepository.findByNotificationType(notificationType)
            .switchIfEmpty(Mono.error(new TemplateNotFoundException()));
    }
}

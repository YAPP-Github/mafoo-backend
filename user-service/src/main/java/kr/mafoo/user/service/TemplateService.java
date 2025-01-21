package kr.mafoo.user.service;

import kr.mafoo.user.domain.TemplateEntity;
import kr.mafoo.user.enums.NotificationType;
import kr.mafoo.user.exception.TemplateNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateQuery templateQuery;
    private final TemplateCommand templateCommand;
    
    @Transactional(readOnly = true)
    public Flux<TemplateEntity> findTemplateList() {
        return templateQuery.findAll()
            .onErrorResume(TemplateNotFoundException.class, ex -> Flux.empty());
    }

    @Transactional(readOnly = true)
    public Mono<TemplateEntity> findTemplateById(
        String templateId
    ) {
        return templateQuery.findById(templateId);
    }

    @Transactional
    public Mono<TemplateEntity> addTemplate(
        NotificationType notificationType,
        String thumbnailImageUrl,
        String title,
        String body
    ) {
        return templateCommand.addTemplate(notificationType, thumbnailImageUrl, title, body);
    }

    @Transactional
    public Mono<TemplateEntity> modifyTemplate(
        String templateId,
        NotificationType notificationType,
        String thumbnailImageUrl,
        String title,
        String body
    ) {
        return templateQuery.findById(templateId)
            .flatMap(template -> templateCommand.modifyTemplate(template, notificationType, thumbnailImageUrl, title, body));
    }

    @Transactional
    public Mono<Void> removeTemplate(
        String templateId
    ) {
        return templateQuery.findById(templateId)
            .flatMap(templateCommand::removeTemplate);
    }
}

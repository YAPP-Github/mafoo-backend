package kr.mafoo.user.service;

import kr.mafoo.user.domain.TemplateEntity;
import kr.mafoo.user.enums.NotificationType;
import kr.mafoo.user.enums.NotificationRoute;
import kr.mafoo.user.repository.TemplateRepository;
import kr.mafoo.user.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TemplateCommand {

    private final TemplateRepository templateRepository;

    public Mono<TemplateEntity> addTemplate(NotificationType notificationType, String icon, String title, String body, NotificationRoute routeType) {
        return templateRepository.save(
            TemplateEntity.newTemplate(IdGenerator.generate(), notificationType, icon, title, body, routeType)
        );
    }

    public Mono<TemplateEntity> modifyTemplate(TemplateEntity template, NotificationType notificationType, String icon, String title, String body, NotificationRoute routeType) {
        return templateRepository.save(
            template.updateTemplate(notificationType, icon, title, body, routeType)
        );
    }

    public Mono<Void> removeTemplate(String templateId) {
        return templateRepository.softDeleteById(templateId);
    }
}

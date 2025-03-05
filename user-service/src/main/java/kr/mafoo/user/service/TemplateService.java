package kr.mafoo.user.service;

import kr.mafoo.user.domain.TemplateEntity;
import kr.mafoo.user.enums.NotificationType;
import kr.mafoo.user.enums.NotificationRoute;
import kr.mafoo.user.exception.TemplateNotFoundException;
import kr.mafoo.user.service.dto.TemplateDetailDto;
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

    private final ReservationQuery reservationQuery;
    
    @Transactional(readOnly = true)
    public Flux<TemplateEntity> findTemplateList() {
        return templateQuery.findAll()
            .onErrorResume(TemplateNotFoundException.class, ex -> Flux.empty());
    }

    @Transactional(readOnly = true)
    public Mono<TemplateDetailDto> findTemplateById(
        String templateId
    ) {
        return templateQuery.findById(templateId)
            .flatMap(template -> reservationQuery.findByTemplateId(template.getTemplateId())
                .collectList()
                .flatMap(reservationList -> Mono.just(TemplateDetailDto.fromEntities(template, reservationList)))
            );
    }

    @Transactional
    public Mono<TemplateEntity> addTemplate(
        NotificationType notificationType,
        String icon,
        String title,
        String body,
        NotificationRoute routeType
    ) {
        return templateCommand.addTemplate(notificationType, icon, title, body, routeType);
    }

    @Transactional
    public Mono<TemplateEntity> modifyTemplate(
        String templateId,
        NotificationType notificationType,
        String icon,
        String title,
        String body,
        NotificationRoute routeType
    ) {
        return templateQuery.findById(templateId)
            .flatMap(template -> templateCommand.modifyTemplate(template, notificationType, icon, title, body, routeType));
    }

    @Transactional
    public Mono<Void> removeTemplate(
        String templateId
    ) {
        return templateCommand.removeTemplate(templateId);
    }
}

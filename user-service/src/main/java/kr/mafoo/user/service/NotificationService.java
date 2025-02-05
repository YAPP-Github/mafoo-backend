package kr.mafoo.user.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import kr.mafoo.user.domain.NotificationEntity;
import kr.mafoo.user.enums.NotificationType;
import kr.mafoo.user.enums.VariableDomain;
import kr.mafoo.user.enums.VariableSort;
import kr.mafoo.user.enums.VariableType;
import kr.mafoo.user.exception.NotificationNotFoundException;
import kr.mafoo.user.service.dto.NotificationDetailDto;
import kr.mafoo.user.service.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationQuery notificationQuery;
    private final NotificationCommand notificationCommand;

    private final TemplateQuery templateQuery;

    private final VariableService variableService;

    @Transactional(readOnly = true)
    public Flux<NotificationDetailDto> findNotificationListByMemberId(String requestMemberId) {
        return notificationQuery.findAllByReceiverMemberId(requestMemberId)
            .onErrorResume(NotificationNotFoundException.class, ex -> Flux.empty())
            .concatMap(notification -> templateQuery.findById(notification.getTemplateId())
                .flatMapMany(template -> Flux.just(NotificationDetailDto.fromEntities(notification, template))));
    }

    @Transactional
    public Flux<NotificationEntity> sendNotificationByScenario(NotificationType notificationType, List<String> receiverMemberIds, Map<String, String> variables) {
        return templateQuery.findByNotificationType(notificationType)
            .flatMapMany(template -> sendNotificationWithVariables(template.getTemplateId(), receiverMemberIds, template.getTitle(), template.getBody(), variables));
    }

    private Flux<NotificationEntity> sendNotificationWithVariables(String templateId, List<String> receiverMemberIds, String title, String body, Map<String, String> variables) {
        return Flux.defer(() -> {
            MessageDto messageDto = MessageDto.fromTemplateWithVariables(receiverMemberIds, title, body, variables);
            return addNotificationBulk(templateId, messageDto);
        });
    }

    @Transactional
    public Flux<NotificationEntity> sendNotificationByReservation(String templateId, String receiverMemberId, VariableDomain domain, VariableSort sort, VariableType type) {
        return templateQuery.findById(templateId)
            .flatMapMany(template -> {
                List<String> receiverMemberIds = Arrays.asList(receiverMemberId.replaceAll("[\\[\\]]", "").split(","));

                return domain.equals(VariableDomain.NONE)
                    ? sendNotificationWithoutVariables(template.getTemplateId(), receiverMemberIds, template.getTitle(), template.getBody())
                    : sendNotificationWithDynamicVariables(template.getTemplateId(), receiverMemberIds, template.getTitle(), template.getBody(), domain, sort, type);
            });
    }

    private Flux<NotificationEntity> sendNotificationWithoutVariables(String templateId, List<String> receiverMemberIds, String title, String body) {
        return Flux.defer(() -> {
            MessageDto messageDto = MessageDto.fromTemplateWithoutVariables(receiverMemberIds, title, body);
            return addNotificationBulk(templateId, messageDto);
        });
    }

    private Flux<NotificationEntity> sendNotificationWithDynamicVariables(String templateId, List<String> receiverMemberIds, String title, String body, VariableDomain domain, VariableSort sort, VariableType type) {
        return Flux.fromIterable(receiverMemberIds)
            .flatMap(receiverMemberId ->
                variableService.getVariableMap(receiverMemberId, domain, sort, type)
                    .map(variableMap -> MessageDto.fromTemplateWithVariables(
                        List.of(receiverMemberId), title, body, variableMap))
            )
            .collectList()
            .flatMapMany(messageDtoList -> addDynamicNotificationBulk(templateId, messageDtoList));
    }

    private Flux<NotificationEntity> addNotificationBulk(String templateId, MessageDto messageDto) {
        return Flux.fromIterable(messageDto.receiverMemberIds())
            .flatMap(receiverMemberId -> notificationCommand.addNotification(
                templateId, receiverMemberId, messageDto.title(), messageDto.body()
            ));
    }

    private Flux<NotificationEntity> addDynamicNotificationBulk(String templateId, List<MessageDto> messageDtos) {
        return Flux.fromIterable(messageDtos)
            .flatMap(messageDto -> notificationCommand.addNotification(
                templateId, messageDto.receiverMemberIds().get(0), messageDto.title(), messageDto.body()
            ));
    }

    @Transactional
    public Flux<NotificationEntity> updateNotificationBulkIsRead(List<String> notificationIds, String requestMemberId) {
        return Flux.fromIterable(notificationIds)
            .concatMap(notificationId -> notificationQuery.findById(notificationId)
                .flatMap(notificationCommand::modifyNotificationIsReadTrue)
            );
    }

    @Transactional
    public Mono<Void> removeNotificationBulk(List<String> notificationIds, String requestMemberId) {
        return Flux.fromIterable(notificationIds)
            .concatMap(notificationId -> notificationQuery.findById(notificationId)
                .flatMap(notificationCommand::removeNotification)
            ).then();
    }
}

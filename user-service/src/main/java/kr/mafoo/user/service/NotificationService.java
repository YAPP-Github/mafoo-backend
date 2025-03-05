package kr.mafoo.user.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import kr.mafoo.user.domain.NotificationEntity;
import kr.mafoo.user.domain.TemplateEntity;
import kr.mafoo.user.enums.NotificationType;
import kr.mafoo.user.enums.VariableDomain;
import kr.mafoo.user.enums.VariableSort;
import kr.mafoo.user.enums.VariableParam;
import kr.mafoo.user.exception.FcmTokenNotFoundException;
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
    private final FcmTokenQuery fcmTokenQuery;
    private final VariableService variableService;
    private final MessageService messageService;

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
            .flatMapMany(template -> sendNotification(receiverMemberIds, template, variables, null, null, null));
    }

    @Transactional
    public Flux<NotificationEntity> sendNotificationByReservation(String templateId, String receiverMemberId, VariableDomain domain, VariableSort sort, VariableParam type) {
        List<String> receiverMemberIds = Arrays.asList(receiverMemberId.replaceAll("[\\[\\]]", "").split(","));

        return templateQuery.findById(templateId)
            .flatMapMany(template -> sendNotification(receiverMemberIds, template, null, domain, sort, type));
    }

    private Flux<NotificationEntity> sendNotification(List<String> receiverMemberIds, TemplateEntity template, Map<String, String> variables, VariableDomain domain, VariableSort sort, VariableParam type) {
        return fcmTokenQuery.findAllByOwnerMemberIdList(receiverMemberIds)
            .onErrorResume(FcmTokenNotFoundException.class, ex -> Flux.empty())
            .flatMap(fcmToken -> getVariableMap(fcmToken.getOwnerMemberId(), variables, domain, sort, type)
                .map(variableMap -> MessageDto.fromEntities(fcmToken, template, variableMap))
            )
            .collectList()
            .flatMapMany(messageDtoList -> addNotificationBulkFromMessageDto(template.getTemplateId(), messageDtoList));
    }

    private Mono<Map<String, String>> getVariableMap(String ownerMemberId, Map<String, String> variables, VariableDomain domain, VariableSort sort, VariableParam type) {
        if (variables != null) {
            return variableService.getVariableMapWithVariables(ownerMemberId, variables);
        } else {
            return variableService.getVariableMapWithDynamicVariables(ownerMemberId, domain, sort, type);
        }
    }

    private Flux<NotificationEntity> addNotificationBulkFromMessageDto(String templateId, List<MessageDto> messageDtoList) {
        return Flux.fromIterable(messageDtoList)
            .flatMap(messageDto -> notificationCommand.addNotification(
                messageDto.notificationId(), templateId, messageDto.receiverMemberId(), messageDto.icon(), messageDto.title(), messageDto.body(), messageDto.paramKey()
            ))
            .flatMap(notifications -> messageService.sendMessageToMultipleMember(messageDtoList)
                .thenReturn(notifications)
            );
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
            .concatMap(notificationCommand::removeNotification).then();
    }
}

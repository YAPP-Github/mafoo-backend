package kr.mafoo.user.service;

import java.util.List;
import kr.mafoo.user.domain.NotificationEntity;
import kr.mafoo.user.exception.NotificationNotFoundException;
import kr.mafoo.user.service.dto.NotificationDetailDto;
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

    @Transactional(readOnly = true)
    public Flux<NotificationDetailDto> findNotificationListByMemberId(String requestMemberId) {
        return notificationQuery.findAllByReceiverMemberId(requestMemberId)
            .onErrorResume(NotificationNotFoundException.class, ex -> Flux.empty())
            .concatMap(notification -> templateQuery.findById(notification.getTemplateId())
                .flatMapMany(template -> Flux.just(NotificationDetailDto.fromEntities(notification, template))));
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

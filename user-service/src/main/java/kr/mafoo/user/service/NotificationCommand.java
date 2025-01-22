package kr.mafoo.user.service;

import kr.mafoo.user.domain.NotificationEntity;
import kr.mafoo.user.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotificationCommand {

    private final NotificationRepository notificationRepository;

    public Mono<NotificationEntity> modifyNotificationIsReadTrue(NotificationEntity notification) {
        return notificationRepository.save(
            notification.updateNotificationIsReadTrue()
        );
    }

    public Mono<Void> removeNotification(NotificationEntity notification) {
        return notificationRepository.delete(notification);
    }
}

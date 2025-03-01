package kr.mafoo.user.service;

import kr.mafoo.user.domain.NotificationEntity;
import kr.mafoo.user.repository.NotificationRepository;
import kr.mafoo.user.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotificationCommand {

    private final NotificationRepository notificationRepository;

    public Mono<NotificationEntity> addNotification(String notificationId, String templateId, String receiverMemberId, String title, String body, String paramKey) {
        return notificationRepository.save(
            NotificationEntity.newNotification(notificationId, templateId, receiverMemberId, title, body, paramKey)
        );
    }

    public Mono<NotificationEntity> modifyNotificationIsReadTrue(NotificationEntity notification) {
        return notificationRepository.save(
            notification.updateNotificationIsReadTrue()
        );
    }

    public Mono<Void> removeNotification(String notificationId) {
        return notificationRepository.softDeleteById(notificationId);
    }
}

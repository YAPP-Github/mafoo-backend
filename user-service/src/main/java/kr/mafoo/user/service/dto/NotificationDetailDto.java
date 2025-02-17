package kr.mafoo.user.service.dto;

import java.time.LocalDateTime;
import kr.mafoo.user.domain.NotificationEntity;
import kr.mafoo.user.domain.TemplateEntity;
import kr.mafoo.user.enums.NotificationType;

public record NotificationDetailDto(
    String notificationId,
    String templateId,
    String receiverMemberId,
    NotificationType notificationType,
    String thumbnailImageUrl,
    String title,
    String body,
    Boolean isRead,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
        public static NotificationDetailDto fromEntities(
            NotificationEntity notificationEntity,
            TemplateEntity templateEntity
        ) {
                return new NotificationDetailDto(
                    notificationEntity.getNotificationId(),
                    notificationEntity.getTemplateId(),
                    notificationEntity.getReceiverMemberId(),
                    templateEntity.getNotificationType(),
                    templateEntity.getThumbnailImageUrl(),
                    notificationEntity.getTitle(),
                    notificationEntity.getBody(),
                    notificationEntity.getIsRead(),
                    notificationEntity.getCreatedAt(),
                    notificationEntity.getUpdatedAt()
                );
        }
}

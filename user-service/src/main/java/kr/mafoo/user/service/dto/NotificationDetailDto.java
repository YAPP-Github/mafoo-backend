package kr.mafoo.user.service.dto;

import java.time.LocalDateTime;
import kr.mafoo.user.domain.NotificationEntity;
import kr.mafoo.user.domain.TemplateEntity;
import kr.mafoo.user.enums.NotificationButton;
import kr.mafoo.user.enums.NotificationIcon;
import kr.mafoo.user.enums.NotificationType;

public record NotificationDetailDto(
    String notificationId,
    String templateId,
    String receiverMemberId,
    NotificationType notificationType,
    NotificationIcon icon,
    String title,
    String body,
    String route,
    String paramKey,
    NotificationButton buttonType,
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
                    notificationEntity.getIcon(),
                    notificationEntity.getTitle(),
                    notificationEntity.getBody(),
                    templateEntity.getRouteType().getRoute(),
                    notificationEntity.getParamKey(),
                    templateEntity.getRouteType().getButtonType(),
                    notificationEntity.getIsRead(),
                    notificationEntity.getCreatedAt(),
                    notificationEntity.getUpdatedAt()
                );
        }
}

package kr.mafoo.user.service.dto;

import java.time.LocalDateTime;
import java.util.List;
import kr.mafoo.user.domain.ReservationEntity;
import kr.mafoo.user.domain.TemplateEntity;
import kr.mafoo.user.enums.NotificationType;
import kr.mafoo.user.enums.NotificationRoute;

public record TemplateDetailDto(
    String templateId,
    NotificationType notificationType,
    String icon,
    String title,
    String body,
    NotificationRoute routeType,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<ReservationDto> reservation
) {
        public static TemplateDetailDto fromEntities(
            TemplateEntity templateEntity,
            List<ReservationEntity> reservationEntity
        ) {
                return new TemplateDetailDto(
                    templateEntity.getTemplateId(),
                    templateEntity.getNotificationType(),
                    templateEntity.getIcon(),
                    templateEntity.getTitle(),
                    templateEntity.getBody(),
                    templateEntity.getRouteType(),
                    templateEntity.getCreatedAt(),
                    templateEntity.getUpdatedAt(),
                    reservationEntity.stream().map(ReservationDto::fromEntity).toList()
                );
        }
}

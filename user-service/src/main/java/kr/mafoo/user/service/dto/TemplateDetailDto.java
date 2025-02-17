package kr.mafoo.user.service.dto;

import java.time.LocalDateTime;
import java.util.List;
import kr.mafoo.user.domain.ReservationEntity;
import kr.mafoo.user.domain.TemplateEntity;
import kr.mafoo.user.enums.NotificationType;

public record TemplateDetailDto(
    String templateId,
    NotificationType notificationType,
    String thumbnailImageUrl,
    String title,
    String body,
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
                    templateEntity.getThumbnailImageUrl(),
                    templateEntity.getTitle(),
                    templateEntity.getBody(),
                    templateEntity.getCreatedAt(),
                    templateEntity.getUpdatedAt(),
                    reservationEntity.stream().map(ReservationDto::fromEntity).toList()
                );
        }
}

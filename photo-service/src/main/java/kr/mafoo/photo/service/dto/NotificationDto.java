package kr.mafoo.photo.service.dto;

import java.time.LocalDateTime;

public record NotificationDto(
    String notificationId,
    String templateId,
    String receiverMemberId,
    String title,
    String body,
    String key,
    Boolean isRead,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}

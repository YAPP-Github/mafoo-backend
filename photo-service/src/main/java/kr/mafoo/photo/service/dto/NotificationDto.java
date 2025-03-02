package kr.mafoo.photo.service.dto;

public record NotificationDto(
    String notificationId,
    String templateId,
    String receiverMemberId,
    String title,
    String body,
    String paramKey,
    Boolean isRead,
    String createdAt,
    String updatedAt
) {
}

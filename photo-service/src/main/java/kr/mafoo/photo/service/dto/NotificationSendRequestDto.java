package kr.mafoo.photo.service.dto;

import java.util.List;
import java.util.Map;
import kr.mafoo.photo.domain.enums.NotificationType;

public record NotificationSendRequestDto(
    NotificationType notificationType,
    List<String> receiverMemberIds,
    Map<String, String> variables
) {
}

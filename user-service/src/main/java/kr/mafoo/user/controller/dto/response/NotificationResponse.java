package kr.mafoo.user.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import kr.mafoo.user.domain.NotificationEntity;

@Schema(description = "알림 응답")
public record NotificationResponse(
    @Schema(description = "알림 ID", example = "test_notification_id")
    String notificationId,

    @Schema(description = "템플릿 ID", example = "test_template_id")
    String templateId,

    @Schema(description = "수신 사용자 ID 목록", example = "test_member_id")
    String receiverMemberId,

    @Schema(description = "제목", example = "\'시금치파슷하\' 앨범을 공유받았어요")
    String title,

    @Schema(description = "내용", example = "공유받은 앨범을 둘러보세요!")
    String body,

    @Schema(description = "알림 paramKey")
    String paramKey,

    @Schema(description = "읽음 여부", example = "true")
    Boolean isRead,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "알림 생성 일시")
    LocalDateTime createdAt,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "알림 수정 일시")
    LocalDateTime updatedAt
) {
        public static NotificationResponse fromEntity(
            NotificationEntity entity
        ) {
                return new NotificationResponse(
                    entity.getNotificationId(),
                    entity.getTemplateId(),
                    entity.getReceiverMemberId(),
                    entity.getTitle(),
                    entity.getBody(),
                    entity.getParamKey(),
                    entity.getIsRead(),
                    entity.getCreatedAt(),
                    entity.getUpdatedAt()
                );
        }
}

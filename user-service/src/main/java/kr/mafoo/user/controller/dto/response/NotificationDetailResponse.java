package kr.mafoo.user.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import kr.mafoo.user.enums.NotificationButton;
import kr.mafoo.user.enums.NotificationIcon;
import kr.mafoo.user.enums.NotificationType;
import kr.mafoo.user.service.dto.NotificationDetailDto;

@Schema(description = "알림 상세 응답")
public record NotificationDetailResponse(
    @Schema(description = "알림 ID", example = "test_notification_id")
    String notificationId,

    @Schema(description = "템플릿 ID", example = "test_template_id")
    String templateId,

    @Schema(description = "수신 사용자 ID", example = "test_member_id")
    String receiverMemberId,

    @Schema(description = "알림 타입(REGULAR, EVENT, ...)", example = "EVENT")
    NotificationType notificationType,

    @Schema(description = "아이콘 종류")
    NotificationIcon icon,

    @Schema(description = "제목", example = "\'시금치파슷하\' 앨범을 공유받았어요")
    String title,

    @Schema(description = "내용", example = "공유받은 앨범을 둘러보세요!")
    String body,

    @Schema(description = "알림 route")
    String route,

    @Schema(description = "알림 paramKey")
    String paramKey,

    @Schema(description = "알림 button 종류")
    NotificationButton buttonType,

    @Schema(description = "읽음 여부", example = "true")
    Boolean isRead,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "알림 생성 일시")
    LocalDateTime createdAt,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "알림 수정 일시")
    LocalDateTime updatedAt
) {
        public static NotificationDetailResponse fromDto(
            NotificationDetailDto dto
        ) {
                return new NotificationDetailResponse(
                    dto.notificationId(),
                    dto.templateId(),
                    dto.receiverMemberId(),
                    dto.notificationType(),
                    dto.icon(),
                    dto.title(),
                    dto.body(),
                    dto.route(),
                    dto.paramKey(),
                    dto.buttonType(),
                    dto.isRead(),
                    dto.createdAt(),
                    dto.updatedAt()
                );
        }
}

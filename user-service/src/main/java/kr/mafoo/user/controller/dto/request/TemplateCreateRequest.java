package kr.mafoo.user.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.user.enums.NotificationType;
import kr.mafoo.user.enums.NotificationRoute;

@Schema(description = "템플릿 생성 요청")
public record TemplateCreateRequest(
    @Schema(description = "알림 타입(REGULAR, EVENT, ...)", example = "EVENT")
    NotificationType notificationType,

    @Schema(description = "아이콘")
    String icon,

    @Schema(description = "제목", example = "{{album_name}} 앨범을 공유받았어요")
    String title,

    @Schema(description = "내용", example = "{{album_name}} 앨범 공유를 수락하시겠어요?")
    String body,

    @Schema(description = "루트 종류", example = "SHARED_MEMBER_INVITATION")
    NotificationRoute routeType
) {
}

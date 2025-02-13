package kr.mafoo.user.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.user.enums.NotificationType;

@Schema(description = "템플릿 생성 요청")
public record TemplateCreateRequest(
    @Schema(description = "알림 타입(REGULAR, EVENT, ...)", example = "EVENT")
    NotificationType notificationType,

    @Schema(description = "썸네일 이미지 URL", example = "thumbnail_image_url")
    String thumbnailImageUrl,

    @Schema(description = "관련 URL")
    String url,

    @Schema(description = "제목", example = "{{album_name}} 앨범을 공유받았어요")
    String title,

    @Schema(description = "내용", example = "{{album_name}} 앨범 공유를 수락하시겠어요?")
    String body
) {
}

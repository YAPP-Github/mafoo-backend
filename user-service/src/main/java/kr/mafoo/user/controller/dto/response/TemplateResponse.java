package kr.mafoo.user.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import kr.mafoo.user.domain.TemplateEntity;
import kr.mafoo.user.enums.NotificationType;

@Schema(description = "템플릿 응답")
public record TemplateResponse(
        @Schema(description = "템플릿 ID", example = "test_alarm_id")
        String templateId,

        @Schema(description = "알림 타입(REGULAR, EVENT, ...)", example = "EVENT")
        NotificationType notificationType,

        @Schema(description = "썸네일 이미지 URL", example = "thumbnail_image_url")
        String thumbnailImageUrl,

        @Schema(description = "제목", example = "{{album_name}} 앨범을 공유받았어요")
        String title,

        @Schema(description = "내용", example = "{{album_name}} 앨범 공유를 수락하시겠어요?")
        String body,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        @Schema(description = "템플릿 생성 일시")
        LocalDateTime createdAt,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        @Schema(description = "템플릿 수정 일시")
        LocalDateTime updatedAt
) {
        public static TemplateResponse fromEntity(
            TemplateEntity entity
        ) {
                return new TemplateResponse(
                    entity.getTemplateId(),
                    entity.getNotificationType(),
                    entity.getThumbnailImageUrl(),
                    entity.getTitle(),
                    entity.getBody(),
                    entity.getCreatedAt(),
                    entity.getUpdatedAt()
                );
        }
}

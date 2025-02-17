package kr.mafoo.user.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import kr.mafoo.user.enums.NotificationType;
import kr.mafoo.user.service.dto.TemplateDetailDto;

@Schema(description = "템플릿 상세 응답")
public record TemplateDetailResponse(
    @Schema(description = "템플릿 ID", example = "test_alarm_id")
    String templateId,

    @Schema(description = "전송 트리거(MANUAL/...)", example = "MANUAL")
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
    LocalDateTime updatedAt,

    List<ReservationResponse> reservation
) {
        public static TemplateDetailResponse fromDto(
            TemplateDetailDto dto
        ) {
                return new TemplateDetailResponse(
                    dto.templateId(),
                    dto.notificationType(),
                    dto.thumbnailImageUrl(),
                    dto.title(),
                    dto.body(),
                    dto.createdAt(),
                    dto.updatedAt(),
                    dto.reservation().stream().map(ReservationResponse::fromDto).toList()
                );
        }
}

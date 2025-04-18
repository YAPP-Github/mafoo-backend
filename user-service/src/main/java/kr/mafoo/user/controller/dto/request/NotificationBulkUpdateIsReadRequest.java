package kr.mafoo.user.controller.dto.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "알림 n건 읽음 여부 수정 요청")
public record NotificationBulkUpdateIsReadRequest(
    @ArraySchema(
        schema = @Schema(description = "알림 ID 목록"),
        arraySchema = @Schema(example = "[\"test_notification_id_1\", \"test_notification_id_2\", \"test_notification_id_3\"]")
    )
    List<String> notificationIds
) {
}

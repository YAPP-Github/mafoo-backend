package kr.mafoo.user.controller.dto.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import kr.mafoo.user.enums.NotificationType;

@Schema(description = "알림 전송 요청")
public record NotificationSendRequest(
    @Schema(description = "알림 종류")
    NotificationType notificationType,

    @ArraySchema(
        schema = @Schema(description = "전송 사용자 ID 목록"),
        arraySchema = @Schema(example = "[\"test_member_id_1\", \"test_member_id_2\", \"test_member_id_3\"]")
    )
    List<String> receiverMemberIds,

    @Schema(description = "변수 목록")
    Map<String, String> variables
) {
}

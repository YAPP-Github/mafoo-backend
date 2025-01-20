package kr.mafoo.user.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import kr.mafoo.user.domain.FcmTokenEntity;

@Schema(description = "FCM 토큰 응답")
public record FcmTokenResponse(
    @Schema(description = "토큰 ID", example = "test_fcm_token_id")
    String fcmTokenId,

    @Schema(description = "토큰 소유자 ID", example = "test_member_id")
    String ownerMemberId,

    @Schema(description = "토큰 값", example = "fcm_token")
    String token,

    @Schema(description = "토큰 생성 일시")
    LocalDateTime createdAt,

    @Schema(description = "토큰 수정 일시")
    LocalDateTime updatedAt
) {
        public static FcmTokenResponse fromEntity(
            FcmTokenEntity entity
        ) {
                return new FcmTokenResponse(
                    entity.getFcmTokenId(),
                    entity.getOwnerMemberId(),
                    entity.getToken(),
                    entity.getCreatedAt(),
                    entity.getUpdatedAt()
                );
        }
}

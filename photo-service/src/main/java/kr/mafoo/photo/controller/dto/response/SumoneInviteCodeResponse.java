package kr.mafoo.photo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.domain.SumoneEventMappingEntity;

@Schema(description = "리캡 응답")
public record SumoneInviteCodeResponse(
        @Schema(description = "초대 코드", example = "ABCDEFGH")
        String code
){
        public static SumoneInviteCodeResponse fromEntity(SumoneEventMappingEntity entity) {
                return new SumoneInviteCodeResponse(entity.getInviteCode());
        }

}

package kr.mafoo.user.service.dto;

import kr.mafoo.user.domain.MemberEntity;

public record ReceiverMemberDto(
    String receiverMemberId,
    String receiverName
) {
    public static ReceiverMemberDto fromEntity(
        MemberEntity entity
    ) {
        return new ReceiverMemberDto(
            entity.getId(),
            entity.getName()
        );
    }
}
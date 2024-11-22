package kr.mafoo.photo.service.dto;

import kr.mafoo.photo.domain.SharedMemberEntity;
import kr.mafoo.photo.domain.enums.PermissionLevel;
import kr.mafoo.photo.domain.enums.ShareStatus;

public record SharedMemberDto(
    String sharedMemberId,
    ShareStatus shareStatus,
    PermissionLevel permissionLevel,
    String memberId,
    String name,
    String profileImageUrl,
    String serialNumber
) {
    public static SharedMemberDto fromSharedMember(
        SharedMemberEntity entity,
        MemberDto dto
    ) {
        return new SharedMemberDto(
            entity.getSharedMemberId(),
            entity.getShareStatus(),
            entity.getPermissionLevel(),
            dto.memberId(),
            dto.profileImageUrl(),
            dto.name(),
            dto.serialNumber()
        );
    }
}

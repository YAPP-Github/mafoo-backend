package kr.mafoo.photo.service.dto;

import kr.mafoo.photo.domain.SharedMemberEntity;
import kr.mafoo.photo.domain.enums.PermissionLevel;
import kr.mafoo.photo.domain.enums.ShareStatus;

public record SharedMemberDetailDto(
    String sharedMemberId,
    ShareStatus shareStatus,
    PermissionLevel permissionLevel,
    String albumId,
    String memberId,
    String profileImageUrl,
    String memberName
) {
    public static SharedMemberDetailDto from(
        SharedMemberEntity sharedMemberEntity,
        MemberDto memberDto
    ) {
        return new SharedMemberDetailDto(
            sharedMemberEntity.getSharedMemberId(),
            sharedMemberEntity.getShareStatus(),
            sharedMemberEntity.getPermissionLevel(),
            sharedMemberEntity.getAlbumId(),
            memberDto.memberId(),
            memberDto.profileImageUrl(),
            memberDto.name()
        );
    }
}

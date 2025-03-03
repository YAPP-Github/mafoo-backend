package kr.mafoo.user.service.dto;

import kr.mafoo.user.domain.MemberEntity;

public record MemberDetailDto(
    String memberId,
    String name,
    String profileImageUrl,
    String serialNumber,
    String sharedMemberId,
    String shareStatus,
    String permissionLevel
) {
    public static MemberDetailDto fromSharedMember(
        MemberEntity memberEntity,
        SharedMemberDto sharedMemberDto
    ) {
        if (sharedMemberDto == null) {
            return new MemberDetailDto(
                memberEntity.getId(),
                memberEntity.getName(),
                memberEntity.getProfileImageUrl(),
                String.format("%04d", memberEntity.getSerialNumber()),
                null,
                null,
                null
            );
        } else {
            return new MemberDetailDto(
                memberEntity.getId(),
                memberEntity.getName(),
                memberEntity.getProfileImageUrl(),
                String.format("%04d", memberEntity.getSerialNumber()),
                sharedMemberDto.sharedMemberId(),
                sharedMemberDto.shareStatus(),
                sharedMemberDto.permissionLevel()
            );
        }
    }
}

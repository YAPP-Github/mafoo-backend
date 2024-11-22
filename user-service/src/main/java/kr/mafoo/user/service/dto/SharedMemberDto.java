package kr.mafoo.user.service.dto;

public record SharedMemberDto(
    String sharedMemberId,
    String shareStatus,
    String permissionLevel,
    String albumId,
    String memberId
) {
}

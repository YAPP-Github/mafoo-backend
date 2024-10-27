package kr.mafoo.photo.service.dto;

public record MemberDto(
        String memberId,
        String name,
        String profileImageUrl
) {
}

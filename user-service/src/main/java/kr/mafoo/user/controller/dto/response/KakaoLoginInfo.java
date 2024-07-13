package kr.mafoo.user.controller.dto.response;

public record KakaoLoginInfo(
        String id,
        String nickname,
        String email,
        String profileImageUrl
) {
}

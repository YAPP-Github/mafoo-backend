package kr.mafoo.user.domain;

public record AuthToken(
        String accessToken,
        String refreshToken
){

}

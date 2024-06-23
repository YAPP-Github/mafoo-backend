package kr.mafoo.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    MEMBER_NOT_FOUND("ME0001", "사용자를 찾을 수 없습니다"),
    KAKAO_LOGIN_FAILED("EX0001", "카카오 로그인에 실패했습니다"),

    TOKEN_TYPE_MISMATCH("AU0001", "토큰 타입이 일치하지 않습니다. (아마 AccessToken?)"),
    TOKEN_EXPIRED("AU0002", "토큰이 만료되었습니다"),
    TOKEN_INVALID("AU0003", "토큰이 유효하지 않습니다"),
    ;
    private final String code;
    private final String message;
}

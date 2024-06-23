package kr.mafoo.user.exception;

public class KakaoLoginFailedException extends DomainException {
    public KakaoLoginFailedException() {
        super(ErrorCode.KAKAO_LOGIN_FAILED);
    }
}

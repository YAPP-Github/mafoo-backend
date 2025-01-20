package kr.mafoo.user.exception;

public class FcmTokenNotFoundException extends DomainException {
    public FcmTokenNotFoundException() {
        super(ErrorCode.FCM_TOKEN_NOT_FOUND);
    }
}

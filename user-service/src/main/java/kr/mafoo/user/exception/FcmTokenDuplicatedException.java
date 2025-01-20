package kr.mafoo.user.exception;

public class FcmTokenDuplicatedException extends DomainException {
    public FcmTokenDuplicatedException() {
        super(ErrorCode.FCM_TOKEN_DUPLICATED);
    }
}

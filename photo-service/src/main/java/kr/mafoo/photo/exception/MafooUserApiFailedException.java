package kr.mafoo.photo.exception;

public class MafooUserApiFailedException extends DomainException {
    public MafooUserApiFailedException() {
        super(ErrorCode.MAFOO_USER_API_FAILED);
    }
}

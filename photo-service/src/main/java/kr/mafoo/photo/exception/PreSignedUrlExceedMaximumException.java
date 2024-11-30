package kr.mafoo.photo.exception;

public class PreSignedUrlExceedMaximumException extends DomainException {
    public PreSignedUrlExceedMaximumException() {
        super(ErrorCode.PRE_SIGNED_URL_EXCEED_MAXIMUM);
    }
}

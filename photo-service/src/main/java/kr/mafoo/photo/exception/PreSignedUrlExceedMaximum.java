package kr.mafoo.photo.exception;

public class PreSignedUrlExceedMaximum extends DomainException {
    public PreSignedUrlExceedMaximum() {
        super(ErrorCode.PRE_SIGNED_URL_EXCEED_MAXIMUM);
    }
}

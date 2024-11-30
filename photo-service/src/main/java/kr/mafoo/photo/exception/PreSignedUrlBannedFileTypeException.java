package kr.mafoo.photo.exception;

public class PreSignedUrlBannedFileTypeException extends DomainException {
    public PreSignedUrlBannedFileTypeException() {
        super(ErrorCode.PRE_SIGNED_URL_BANNED_FILE_TYPE);
    }
}

package kr.mafoo.photo.exception;

public class PreSignedUrlBannedFileType extends DomainException {
    public PreSignedUrlBannedFileType() {
        super(ErrorCode.PRE_SIGNED_URL_BANNED_FILE_TYPE);
    }
}

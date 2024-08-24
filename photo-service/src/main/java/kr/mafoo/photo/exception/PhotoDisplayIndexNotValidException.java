package kr.mafoo.photo.exception;

public class PhotoDisplayIndexNotValidException extends DomainException {
    public PhotoDisplayIndexNotValidException() {
        super(ErrorCode.PHOTO_DISPLAY_INDEX_NOT_VALID);
    }
}

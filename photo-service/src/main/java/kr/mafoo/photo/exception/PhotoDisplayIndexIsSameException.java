package kr.mafoo.photo.exception;

public class PhotoDisplayIndexIsSameException extends DomainException {
    public PhotoDisplayIndexIsSameException() {
        super(ErrorCode.PHOTO_DISPLAY_INDEX_IS_SAME);
    }
}

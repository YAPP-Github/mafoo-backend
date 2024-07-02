package kr.mafoo.photo.exception;

public class PhotoNotFoundException extends DomainException {
    public PhotoNotFoundException() {
        super(ErrorCode.PHOTO_NOT_FOUND);
    }
}

package kr.mafoo.photo.exception;

public class PhotoBrandNotExistsException extends DomainException {
    public PhotoBrandNotExistsException() {
        super(ErrorCode.PHOTO_BRAND_NOT_EXISTS);
    }
}

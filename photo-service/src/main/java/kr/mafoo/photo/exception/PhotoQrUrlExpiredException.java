package kr.mafoo.photo.exception;

public class PhotoQrUrlExpiredException extends DomainException{
    public PhotoQrUrlExpiredException() {
        super(ErrorCode.PHOTO_QR_URL_EXPIRED);
    }
}

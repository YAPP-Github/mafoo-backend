package kr.mafoo.photo.exception;

public class RecapPhotoCountNotValidException extends DomainException {
    public RecapPhotoCountNotValidException() {
        super(ErrorCode.RECAP_PHOTO_COUNT_NOT_VALID);
    }
}

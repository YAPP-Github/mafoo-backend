package kr.mafoo.user.exception;

public class MafooPhotoApiFailedException extends DomainException {
    public MafooPhotoApiFailedException() {
        super(ErrorCode.MAFOO_PHOTO_API_FAILED);
    }
}

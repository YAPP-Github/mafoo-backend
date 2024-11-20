package kr.mafoo.photo.exception;

public class PhotoOwnerAlreadyAssignedException extends DomainException {
    public PhotoOwnerAlreadyAssignedException() {
        super(ErrorCode.PHOTO_OWNER_ALREADY_ASSIGNED);
    }
}

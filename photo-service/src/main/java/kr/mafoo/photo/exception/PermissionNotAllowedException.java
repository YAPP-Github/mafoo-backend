package kr.mafoo.photo.exception;

public class PermissionNotAllowedException extends DomainException {
    public PermissionNotAllowedException() {
        super(ErrorCode.PERMISSION_NOT_ALLOWED);
    }
}

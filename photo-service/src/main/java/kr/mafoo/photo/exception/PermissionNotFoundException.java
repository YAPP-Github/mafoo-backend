package kr.mafoo.photo.exception;

public class PermissionNotFoundException extends DomainException {
    public PermissionNotFoundException() {
        super(ErrorCode.PERMISSION_NOT_FOUND);
    }
}

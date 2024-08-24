package kr.mafoo.photo.exception;

public class PermissionAlreadyExistsException extends DomainException {
    public PermissionAlreadyExistsException() {
        super(ErrorCode.PERMISSION_ALREADY_EXISTS);
    }
}

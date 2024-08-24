package kr.mafoo.photo.exception;

public class CannotMakePermissionMyselfException extends DomainException {
    public CannotMakePermissionMyselfException() {
        super(ErrorCode.CANNOT_MAKE_PERMISSION_MYSELF);
    }
}

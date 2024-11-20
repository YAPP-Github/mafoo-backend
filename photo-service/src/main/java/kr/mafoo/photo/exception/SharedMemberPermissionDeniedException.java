package kr.mafoo.photo.exception;

public class SharedMemberPermissionDeniedException extends DomainException {
    public SharedMemberPermissionDeniedException() {
        super(ErrorCode.SHARED_MEMBER_PERMISSION_DENIED);
    }
}

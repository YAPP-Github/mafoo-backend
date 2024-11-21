package kr.mafoo.photo.exception;

public class SharedMemberStatusNotAcceptedException extends DomainException {
    public SharedMemberStatusNotAcceptedException() {
        super(ErrorCode.SHARED_MEMBER_STATUS_NOT_ACCEPTED);
    }
}

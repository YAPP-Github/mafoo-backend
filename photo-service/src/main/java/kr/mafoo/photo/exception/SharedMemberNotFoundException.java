package kr.mafoo.photo.exception;

public class SharedMemberNotFoundException extends DomainException {
    public SharedMemberNotFoundException() {
        super(ErrorCode.SHARED_MEMBER_NOT_FOUND);
    }
}

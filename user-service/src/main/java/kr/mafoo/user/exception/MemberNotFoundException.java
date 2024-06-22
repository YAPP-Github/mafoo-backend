package kr.mafoo.user.exception;

public class MemberNotFoundException extends DomainException {
    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}

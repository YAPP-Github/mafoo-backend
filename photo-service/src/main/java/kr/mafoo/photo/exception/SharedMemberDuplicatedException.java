package kr.mafoo.photo.exception;

public class SharedMemberDuplicatedException extends DomainException {
    public SharedMemberDuplicatedException() {
        super(ErrorCode.SHARED_MEMBER_DUPLICATED);
    }
}

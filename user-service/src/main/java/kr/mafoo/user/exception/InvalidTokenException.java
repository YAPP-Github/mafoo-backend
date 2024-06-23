package kr.mafoo.user.exception;

public class InvalidTokenException extends DomainException {
    public InvalidTokenException() {
        super(ErrorCode.TOKEN_INVALID);
    }
}

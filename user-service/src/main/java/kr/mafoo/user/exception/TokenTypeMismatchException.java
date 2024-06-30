package kr.mafoo.user.exception;

public class TokenTypeMismatchException extends DomainException {
    public TokenTypeMismatchException() {
        super(ErrorCode.TOKEN_TYPE_MISMATCH);
    }
}

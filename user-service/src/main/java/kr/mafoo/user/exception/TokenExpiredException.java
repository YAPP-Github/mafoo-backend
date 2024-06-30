package kr.mafoo.user.exception;

public class TokenExpiredException extends DomainException {
    public TokenExpiredException() {
        super(ErrorCode.TOKEN_EXPIRED);
    }
}

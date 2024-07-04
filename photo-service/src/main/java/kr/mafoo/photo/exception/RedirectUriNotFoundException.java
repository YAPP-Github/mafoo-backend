package kr.mafoo.photo.exception;

public class RedirectUriNotFoundException extends DomainException {
    public RedirectUriNotFoundException() {
        super(ErrorCode.REDIRECT_URI_NOT_FOUND);
    }
}

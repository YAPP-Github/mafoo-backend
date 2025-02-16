package kr.mafoo.user.exception;

public class NotificationNotFoundException extends DomainException {
    public NotificationNotFoundException() {
        super(ErrorCode.NOTIFICATION_NOT_FOUND);
    }
}

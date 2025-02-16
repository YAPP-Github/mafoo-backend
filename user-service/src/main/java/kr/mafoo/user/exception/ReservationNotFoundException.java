package kr.mafoo.user.exception;

public class ReservationNotFoundException extends DomainException {
    public ReservationNotFoundException() {
        super(ErrorCode.RESERVATION_NOT_FOUND);
    }
}

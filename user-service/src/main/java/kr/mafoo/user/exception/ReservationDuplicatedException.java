package kr.mafoo.user.exception;

public class ReservationDuplicatedException extends DomainException {
    public ReservationDuplicatedException() {
        super(ErrorCode.RESERVATION_DUPLICATED);
    }
}

package kr.mafoo.photo.exception;

public class MafooUserApiFailed extends DomainException {
    public MafooUserApiFailed() {
        super(ErrorCode.MAFOO_USER_API_FAILED);
    }
}

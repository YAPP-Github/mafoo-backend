package kr.mafoo.photo.exception;

public class MafooRecapLambdaApiFailedException extends DomainException {
    public MafooRecapLambdaApiFailedException() {
        super(ErrorCode.MAFOO_RECAP_LAMBDA_API_FAILED);
    }
}

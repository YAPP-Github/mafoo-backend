package kr.mafoo.user.exception;

public class TemplateNotFoundException extends DomainException {
    public TemplateNotFoundException() {
        super(ErrorCode.TEMPLATE_NOT_FOUND);
    }
}

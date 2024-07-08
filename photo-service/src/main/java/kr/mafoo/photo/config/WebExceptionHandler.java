package kr.mafoo.photo.config;

import jakarta.validation.ConstraintViolationException;
import kr.mafoo.photo.controller.dto.response.ErrorResponse;
import kr.mafoo.photo.exception.DomainException;
import kr.mafoo.photo.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

@RestControllerAdvice
public class WebExceptionHandler {
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException exception) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.fromErrorCode(exception.getErrorCode()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            WebExchangeBindException.class})
    public ResponseEntity<ErrorResponse> validException(Exception ex) {
        String errorMessage = "입력값 검증 오류: ";
        if (ex instanceof MethodArgumentNotValidException mex) {
            errorMessage += mex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        } else if (ex instanceof ConstraintViolationException cvex) {
            errorMessage += cvex.getConstraintViolations().iterator().next().getMessage();
        } else if (ex instanceof WebExchangeBindException wex) {
            errorMessage += wex.getAllErrors().get(0).getDefaultMessage();
        } else {
            errorMessage += "알 수 없는 오류";
        }
        ErrorResponse response = new ErrorResponse(
                ErrorCode.REQUEST_INPUT_NOT_VALID.getCode(),
                errorMessage
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }
}

package kr.mafoo.photo.config;

import kr.mafoo.photo.controller.dto.response.ErrorResponse;
import kr.mafoo.photo.exception.DomainException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WebExceptionHandler {
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException exception) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.fromErrorCode(exception.getErrorCode()));
    }
}

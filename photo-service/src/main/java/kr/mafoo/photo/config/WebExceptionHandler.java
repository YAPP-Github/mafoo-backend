package kr.mafoo.photo.config;

import jakarta.validation.ConstraintViolationException;
import kr.mafoo.photo.controller.dto.response.ErrorResponse;
import kr.mafoo.photo.exception.DomainException;
import kr.mafoo.photo.exception.ErrorCode;
import kr.mafoo.photo.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RestControllerAdvice
@RequiredArgsConstructor
public class WebExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(WebExceptionHandler.class);
    private final SlackService slackService;

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

    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<String>> handleResponseStatusException(ServerWebExchange exchange, ResponseStatusException exception) {
        return handleExceptionInternal(exchange, exception, (HttpStatus) exception.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleGenericException(ServerWebExchange exchange, Exception exception) {
        return handleExceptionInternal(exchange, exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Mono<ResponseEntity<String>> handleExceptionInternal(ServerWebExchange exchange, Exception exception, HttpStatus status) {
        String method = extractMethod(exchange);
        String userAgent = extractUserAgent(exchange);
        String fullPath = extractFullPath(exchange);
        String originIp = extractOriginIp(exchange);

        return extractRequestBody(exchange).flatMap(requestBody -> {

            logException(method, fullPath, originIp, userAgent, exception);

            if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
                return slackService.sendErrorNotification(
                        method, fullPath, requestBody, originIp, userAgent, exception.getMessage()
                ).then(Mono.just(new ResponseEntity<>("Internal Server Error", status)));
            }

            return Mono.just(new ResponseEntity<>(status.getReasonPhrase(), status));
        });
    }

    private String extractMethod(ServerWebExchange exchange) {
        return exchange.getRequest().getMethod().toString();
    }

    private String extractUserAgent(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst("User-Agent");
    }

    private String extractFullPath(ServerWebExchange exchange) {
        var request = exchange.getRequest();
        String fullPath = request.getURI().getRawPath();
        String query = request.getURI().getQuery();
        return (query != null && !query.isEmpty()) ? fullPath + "?" + query : fullPath;
    }

    private String extractOriginIp(ServerWebExchange exchange) {
        String proxyIp = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
        return Optional.ofNullable(proxyIp)
                .orElseGet(() -> remoteAddress != null ? remoteAddress.getAddress().getHostAddress() : "UNKNOWN SOURCE");
    }

    private Mono<String> extractRequestBody(ServerWebExchange exchange) {
        return exchange.getRequest().getBody().map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);

                    return new String(bytes, StandardCharsets.UTF_8);
                }).reduce(new StringBuilder(), StringBuilder::append)
                .map(StringBuilder::toString);
    }

    private void logException(String method, String fullPath, String originIp, String userAgent, Exception exception) {
        logger.error("Exception occurred: {} {} {} ERROR {} {}", method, fullPath, originIp, exception.getMessage(), userAgent);
    }
}

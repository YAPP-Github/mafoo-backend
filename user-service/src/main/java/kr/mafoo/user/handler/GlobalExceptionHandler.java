package kr.mafoo.user.handler;

import kr.mafoo.user.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final SlackService slackService;

    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<String>> handleResponseStatusException(ServerWebExchange exchange, ResponseStatusException ex) {
        return handleExceptionInternal(exchange, ex, (HttpStatus) ex.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleGenericException(ServerWebExchange exchange, Exception ex) {
        return handleExceptionInternal(exchange, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Mono<ResponseEntity<String>> handleExceptionInternal(ServerWebExchange exchange, Exception ex, HttpStatus status) {
        String method = exchange.getRequest().getMethod().toString();
        String userAgent = exchange.getRequest().getHeaders().getFirst("User-Agent");
        String proxyIp = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        InetSocketAddress address = exchange.getRequest().getRemoteAddress();
        String originIp = proxyIp != null ? proxyIp : (address != null ? address.toString() : "UNKNOWN SOURCE");
        String fullPath = exchange.getRequest().getURI().getPath() +
                (exchange.getRequest().getURI().getQuery() != null ? "?" + exchange.getRequest().getURI().getQuery() : "");

        logger.error("Exception occurred: {} {} {} ERROR {} {}", method, fullPath, originIp, ex.getMessage(), userAgent);

        if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            return slackService.sendErrorNotification(
                    method,
                    fullPath,
                    originIp,
                    userAgent,
                    ex.getMessage()
            ).then(Mono.just(new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR)));
        }

        return Mono.just(new ResponseEntity<>(status.getReasonPhrase(), status));
    }
}

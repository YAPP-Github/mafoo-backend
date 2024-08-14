package kr.mafoo.photo.handler;

import kr.mafoo.photo.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final SlackService slackService;

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleException(ServerWebExchange exchange, Exception ex) {
        String method = exchange.getRequest().getMethod().toString();
        String userAgent = exchange.getRequest().getHeaders().getFirst("User-Agent");
        String proxyIp = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        InetSocketAddress address = exchange.getRequest().getRemoteAddress();
        String originIp = proxyIp != null ? proxyIp : (address != null ? address.toString() : "UNKNOWN SOURCE");
        String fullPath = exchange.getRequest().getURI().getPath() +
                (exchange.getRequest().getURI().getQuery() != null ? "?" + exchange.getRequest().getURI().getQuery() : "");

        logger.error("Exception occurred: {} {} {} ERROR {} {}", method, fullPath, originIp, ex.getMessage(), userAgent);

        return slackService.sendErrorNotification(
                method,
                fullPath,
                originIp,
                userAgent,
                ex.getMessage()
        ).then(
                Mono.just(new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR))
        );
    }
}

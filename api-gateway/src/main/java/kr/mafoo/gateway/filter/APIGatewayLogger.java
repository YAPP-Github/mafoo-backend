package kr.mafoo.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

@Component
public class APIGatewayLogger implements GlobalFilter {
    private final Logger logger = LoggerFactory.getLogger(APIGatewayLogger.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String userAgent = request.getHeaders().getFirst("User-Agent");
        String proxyIp = request.getHeaders().getFirst("X-Forwarded-For");
        InetSocketAddress address =  request.getRemoteAddress();
        String originIp = proxyIp != null ? proxyIp : (address != null ? address.toString() : "UNKNOWN SOURCE");
        String fullPath = request.getURI().getPath() + (request.getURI().getQuery() != null ? "?" + request.getURI().getQuery() : "");
        return chain
                .filter(exchange)
                .doOnSuccess(resVoid -> {
                    long executionTime = System.currentTimeMillis() - startTime;
                    logger.info("{} {} {} {} {}ms {}", request.getMethod(), fullPath, originIp,
                            response.getStatusCode(), executionTime, userAgent);
                });
    }
}

package kr.mafoo.gateway.filter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Hints;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class AdminAuthenticationFilter extends AbstractGatewayFilterFactory<Object> {
    @Qualifier("externalWebClient")
    private final WebClient webClient;

    public AdminAuthenticationFilter(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                return createSimpleErrorResponse(exchange, "AU0001", "인증(Authorization) 헤더가 없습니다");

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String token = authorizationHeader.replace("Bearer ", "");

            return validateUserFromGoogleToken(exchange, chain, token);
        };
    }

    private Mono<Void> validateUserFromGoogleToken(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
        return webClient.get()
                .uri("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + token)
                .retrieve()
                .onStatus(status -> status.value() == 401, res -> Mono.error(new RuntimeException("AUTH")))
                .bodyToMono(Map.class)
                .flatMap(userInfo -> {
                    if (userInfo.containsKey("id")) {
                        String id = (String) userInfo.get("id");
                        if(id.equals("111417958243788309308")) {
                            return chain.filter(exchange);
                        }
                    }
                    return createSimpleErrorResponse(exchange, "AU0004", "인증에 실패했습니다");
                })
                .onErrorResume(error -> error.getMessage().equals("AUTH"),
                        error -> createSimpleErrorResponse(exchange, "AU0004", "인증에 실패했습니다"));
    }

    private Mono<Void> createSimpleErrorResponse(ServerWebExchange exchange, String code, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> errorMap = Map.of("code", code, "message", message);

        return response.writeWith(Mono.fromSupplier(() -> new Jackson2JsonEncoder().encodeValue(errorMap,
                response.bufferFactory(),
                ResolvableType.forInstance(errorMap),
                MediaType.APPLICATION_JSON,
                Hints.from(Hints.LOG_PREFIX_HINT, exchange.getLogPrefix())
        )));
    }
}

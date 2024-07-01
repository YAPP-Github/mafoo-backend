package kr.mafoo.gateway.filter;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;
import java.util.Map;

@Component
public class JWTAuthenticationFilter extends AbstractGatewayFilterFactory<Object> {
    @Value("${app.jwt.verify-key}")
    private String verifyKey;

    private JwtParser parser = null;

    private final static String TOKEN_TYPE_HEADER_KEY = "tkn_typ";
    private final static String REFRESH_TOKEN_TYPE_VALUE = "refresh";
    private final static String USER_ID_CLAIM_KEY = "user_id";
    private final static String MEMBER_ID_HEADER_KEY = "X-MEMBER-ID";

    @PostConstruct
    public void initSignKey() {
        parser = Jwts
                .parser()
                .decryptWith(new SecretKeySpec(verifyKey.getBytes(), "AES"))
                .build();
    }

    private Mono<Void> extractUserIdFromRefreshToken(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
        return Mono.defer(() -> {
            Jwe<Claims> claims = parser.parseEncryptedClaims(token);
            String type = (String) claims.getHeader().get(TOKEN_TYPE_HEADER_KEY);
            if (!type.equals(REFRESH_TOKEN_TYPE_VALUE)) {
                return createSimpleErrorResponse(exchange, "AU0005", "토큰 타입이 올바르지 않습니다");
            }
            String userId = claims.getPayload().get(USER_ID_CLAIM_KEY, String.class);
            exchange.getRequest().mutate().header(MEMBER_ID_HEADER_KEY, userId);
            return chain.filter(exchange);
        }).onErrorResume((error) -> {
            if (error instanceof ExpiredJwtException)
                return createSimpleErrorResponse(exchange, "AU0002", "토큰이 만료되었습니다");
            else if (error instanceof MalformedJwtException)
                return createSimpleErrorResponse(exchange, "AU0003", "토큰이 올바르지 않습니다");
            else
                return createSimpleErrorResponse(exchange, "AU0004", "인증에 실패했습니다");
        });
    }


    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                return createSimpleErrorResponse(exchange, "AU0001", "인증(Authorization) 헤더가 없습니다");

            if(request.getHeaders().containsKey(MEMBER_ID_HEADER_KEY)) //취약점 시도?
                return createSimpleErrorResponse(exchange, "AU0000", "인증 실패");

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String token = authorizationHeader.replace("Bearer ", "");

            return extractUserIdFromRefreshToken(exchange, chain, token);
        };
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

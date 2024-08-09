package kr.mafoo.user.controller;

import kr.mafoo.user.api.AuthApi;
import kr.mafoo.user.controller.dto.request.AppleLoginRequest;
import kr.mafoo.user.controller.dto.request.KakaoLoginRequest;
import kr.mafoo.user.controller.dto.request.TokenRefreshRequest;
import kr.mafoo.user.controller.dto.response.LoginResponse;
import kr.mafoo.user.domain.AuthToken;
import kr.mafoo.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class AuthController implements AuthApi {
    private final AuthService authService;

    @Override
    public Mono<LoginResponse> loginWithKakao(KakaoLoginRequest request, ServerWebExchange exchange) {
        String userAgent = getUserAgent(exchange);
        return authService
                .loginWithKakao(request.accessToken(), userAgent)
                .map(this::toLoginResponse);
    }

    @Override
    public Mono<LoginResponse> loginWithApple(AppleLoginRequest request, ServerWebExchange exchange) {
        String userAgent = getUserAgent(exchange);
        return authService
                .loginWithApple(request.identityToken(), userAgent)
                .map(this::toLoginResponse);
    }

    @Override
    public Mono<LoginResponse> loginWithRefreshToken(TokenRefreshRequest request) {
        return authService
                .loginWithRefreshToken(request.refreshToken())
                .map(this::toLoginResponse);
    }

    private String getUserAgent(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst("User-Agent");
    }

    private LoginResponse toLoginResponse(AuthToken authToken) {
        return new LoginResponse(authToken.accessToken(), authToken.refreshToken());
    }

}

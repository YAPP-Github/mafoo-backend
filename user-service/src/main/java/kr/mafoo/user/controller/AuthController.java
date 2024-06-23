package kr.mafoo.user.controller;

import kr.mafoo.user.api.AuthApi;
import kr.mafoo.user.controller.dto.request.KakaoLoginRequest;
import kr.mafoo.user.controller.dto.request.TokenRefreshRequest;
import kr.mafoo.user.controller.dto.response.LoginResponse;
import kr.mafoo.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class AuthController implements AuthApi {
    private final AuthService authService;

    @Override
    public Mono<LoginResponse> loginWithKakao(KakaoLoginRequest request) {
        return authService
                .loginWithKakao(request.code())
                .map(authToken -> new LoginResponse(authToken.accessToken(), authToken.refreshToken()));
    }

    @Override
    public Mono<LoginResponse> loginWithRefreshToken(TokenRefreshRequest request) {
        return Mono.just(new LoginResponse("test_access_token", request.refreshToken()));
    }
}

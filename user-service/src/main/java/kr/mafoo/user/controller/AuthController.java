package kr.mafoo.user.controller;

import kr.mafoo.user.api.AuthApi;
import kr.mafoo.user.controller.dto.request.KakaoLoginRequest;
import kr.mafoo.user.controller.dto.request.TokenRefreshRequest;
import kr.mafoo.user.controller.dto.response.LoginResponse;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AuthController implements AuthApi {
    @Override
    public Mono<LoginResponse> loginWithKakao(KakaoLoginRequest request) {
        return Mono.just(new LoginResponse("test_access_token", "test_refresh_token"));
    }

    @Override
    public Mono<LoginResponse> loginWithRefreshToken(TokenRefreshRequest request) {
        return Mono.just(new LoginResponse("test_access_token", request.refreshToken()));
    }
}

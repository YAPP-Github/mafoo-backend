package kr.mafoo.user.controller;

import kr.mafoo.user.api.AuthApi;
import kr.mafoo.user.controller.dto.request.LoginRequest;
import kr.mafoo.user.controller.dto.response.LoginResponse;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AuthController implements AuthApi {
    @Override
    public Mono<LoginResponse> login(LoginRequest request) {
        return Mono.empty();
    }
}

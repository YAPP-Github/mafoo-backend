package kr.mafoo.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.mafoo.user.controller.dto.request.AppleLoginRequest;
import kr.mafoo.user.controller.dto.request.KakaoLoginRequest;
import kr.mafoo.user.controller.dto.request.TokenRefreshRequest;
import kr.mafoo.user.controller.dto.response.LoginResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Tag(name = "인증(로그인) 관련 API", description = "토큰 발행, 로그인 등 API")
@Validated
@RequestMapping("/v1/auth")
public interface AuthApi {
    @Operation(summary = "카카오 로그인", description = "카카오 인가 코드로 로그인(토큰 발행)합니다.")
    @PostMapping("/login/kakao")
    Mono<LoginResponse> loginWithKakao(
            @RequestBody KakaoLoginRequest request,
            ServerWebExchange exchange
    );

    @Operation(summary = "애플 로그인" , description = "애플 인가 코드로 로그인(토큰 발행)합니다.")
    @PostMapping("/login/apple")
    Mono<LoginResponse> loginWithApple(
            @RequestBody AppleLoginRequest request,
            ServerWebExchange exchange
    );

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰으로 기존 토큰을 갱신합니다.")
    @PostMapping("/refresh")
    Mono<LoginResponse> loginWithRefreshToken(
            @RequestBody TokenRefreshRequest request
    );
}

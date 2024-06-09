package kr.mafoo.user.api;

import io.swagger.v3.oas.annotations.Operation;
import kr.mafoo.user.controller.dto.request.LoginRequest;
import kr.mafoo.user.controller.dto.response.LoginResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping("/v1/auth")
public interface AuthApi {
    @Operation(summary = "로그인", description = "로그인 테스트 설명입니다.")
    @GetMapping("/login")
    Mono<LoginResponse> login(
            @RequestBody LoginRequest request
    );
}

package kr.mafoo.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.mafoo.user.annotation.RequestMemberId;
import kr.mafoo.user.controller.dto.request.ChangeNameRequest;
import kr.mafoo.user.controller.dto.response.MemberResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "로그인 사용자 정보 API", description = "현재 토큰 주인의 정보를 다루는 API")
@Validated
@RequestMapping("/v1/me")
public interface MeApi {
    @Operation(summary = "내 정보 조회", description = "현재 토큰 주인의 정보를 조회합니다.")
    @GetMapping
    Mono<MemberResponse> getMemberWhoRequested(
            @RequestMemberId @Parameter(hidden = true) String memberId
    );

    @Operation(summary = "탈퇴", description = "현재 토큰 주인이 탈퇴합니다.")
    @DeleteMapping
    Mono<Void> deleteMemberWhoRequested(
            @RequestMemberId @Parameter(hidden = true) String memberId,
            ServerHttpRequest serverHttpRequest
    );

    @Operation(summary = "이름 변경", description = "현재 토큰 주인의 이름을 변경합니다.")
    @PostMapping("/name")
    Mono<MemberResponse> changeName(
            @RequestMemberId @Parameter(hidden = true) String memberId,
            @RequestBody ChangeNameRequest name
    );
}

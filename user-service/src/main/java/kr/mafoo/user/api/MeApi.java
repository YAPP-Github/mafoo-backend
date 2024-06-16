package kr.mafoo.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.mafoo.user.controller.dto.response.MemberResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Tag(name = "로그인 사용자 정보 API", description = "현재 토큰 주인의 정보를 다루는 API")
@RequestMapping("/v1/me")
public interface MeApi {
    @Operation(summary = "내 정보 조회", description = "현재 토큰 주인의 정보를 조회합니다.")
    @GetMapping
    Mono<MemberResponse> getMemberWhoRequested();

    @Operation(summary = "탈퇴", description = "현재 토큰 주인이 탈퇴합니다.")
    @PostMapping("/quit")
    Mono<Void> deleteMemberWhoRequested();
}

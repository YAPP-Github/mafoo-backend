package kr.mafoo.user.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.mafoo.user.controller.dto.response.MemberResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Tag(name = "로그인 사용자 정보 API", description = "현재 토큰 주인의 정보를 다루는 API")
@RequestMapping("/v1/me")
public interface MeApi {
    @GetMapping
    Mono<MemberResponse> getMemberWhoRequested();

    @PostMapping("/quit")
    Mono<Void> deleteMemberWhoRequested();
}

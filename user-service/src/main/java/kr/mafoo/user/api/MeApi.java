package kr.mafoo.user.api;

import kr.mafoo.user.controller.dto.response.MemberResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping("/v1/me")
public interface MeApi {
    @GetMapping
    Mono<MemberResponse> getMemberWhoRequested();

    @PostMapping("/quit")
    Mono<Void> deleteMemberWhoRequested();
}

package kr.mafoo.user.controller;

import kr.mafoo.user.api.MeApi;
import kr.mafoo.user.controller.dto.request.ChangeNameRequest;
import kr.mafoo.user.controller.dto.response.MeResponse;
import kr.mafoo.user.controller.dto.response.MemberResponse;
import kr.mafoo.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class MeController implements MeApi {
    private final MemberService memberService;

    @Override
    public Mono<MeResponse> getMemberWhoRequested(String memberId) {
        return memberService
                .getMemberDetailByMemberId(memberId)
                .map(MeResponse::fromDto);
    }

    @Override
    public Mono<Void> deleteMemberWhoRequested(
        String memberId,
        ServerHttpRequest serverHttpRequest
    ) {
        String authorizationToken = serverHttpRequest.getHeaders().getFirst("Authorization");

        return memberService
                .quitMemberByMemberId(memberId, authorizationToken);
    }

    @Override
    public Mono<MemberResponse> changeName(String memberId, ChangeNameRequest name) {
        return memberService
                .changeName(memberId, name.name())
                .map(MemberResponse::fromEntity);
    }
}

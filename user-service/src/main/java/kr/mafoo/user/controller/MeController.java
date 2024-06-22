package kr.mafoo.user.controller;

import kr.mafoo.user.api.MeApi;
import kr.mafoo.user.controller.dto.response.MemberResponse;
import kr.mafoo.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class MeController implements MeApi {
    private final MemberService memberService;

    @Override
    public Mono<MemberResponse> getMemberWhoRequested(String memberId) {
        return memberService
                .getMemberByMemberId(memberId)
                .map(MemberResponse::fromEntity);
    }

    @Override
    public Mono<Void> deleteMemberWhoRequested(String memberId) {
        return memberService
                .quitMemberByMemberId(memberId);
    }
}

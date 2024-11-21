package kr.mafoo.user.controller;

import kr.mafoo.user.api.MemberApi;
import kr.mafoo.user.controller.dto.response.MemberResponse;
import kr.mafoo.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class MemberController implements MemberApi {
    private final MemberService memberService;

    @Override
    public Flux<MemberResponse> getMemberListByName(
        String requesterId,
        String keyword
    ) {
        return memberService.getMemberByKeyword(keyword)
            .map(MemberResponse::fromEntity);
    }

    @Override
    public Mono<MemberResponse> getMember(
        String requesterId,
        String memberId
    ) {
        return memberService.getMemberByMemberId(memberId)
            .map(MemberResponse::fromEntity);
    }
}

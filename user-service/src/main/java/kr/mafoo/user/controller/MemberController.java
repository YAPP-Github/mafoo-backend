package kr.mafoo.user.controller;

import kr.mafoo.user.api.MemberApi;
import kr.mafoo.user.controller.dto.response.MemberDetailResponse;
import kr.mafoo.user.controller.dto.response.MemberResponse;
import kr.mafoo.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class MemberController implements MemberApi {
    private final MemberService memberService;

    @Override
    public Flux<MemberDetailResponse> getMemberListByNameForSharedAlbum(
        String requesterId,
        String keyword,
        String albumId
    ) {
        return memberService.getMemberByKeywordForSharedAlbum(keyword, albumId, requesterId)
            .map(MemberDetailResponse::fromDto);
    }

    @Override
    public Mono<MemberResponse> getMember(
        String memberId
    ) {
        return memberService.getMemberByMemberId(memberId)
            .map(MemberResponse::fromEntity);
    }
}

package kr.mafoo.user.controller;

import kr.mafoo.user.api.MeApi;
import kr.mafoo.user.controller.dto.response.MemberResponse;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class MeController implements MeApi {
    @Override
    public Mono<MemberResponse> getMemberWhoRequested() {
        return Mono.just(new MemberResponse("test", "송영민"));
    }

    @Override
    public Mono<Void> deleteMemberWhoRequested() {
        return Mono.empty();
    }
}

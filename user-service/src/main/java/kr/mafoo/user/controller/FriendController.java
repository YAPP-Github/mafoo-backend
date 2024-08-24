package kr.mafoo.user.controller;

import kr.mafoo.user.api.FriendApi;
import kr.mafoo.user.controller.dto.request.BreakFriendRequest;
import kr.mafoo.user.controller.dto.response.MemberResponse;
import kr.mafoo.user.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class FriendController implements FriendApi {
    private final FriendService friendService;

    @Override
    public Flux<MemberResponse> getFriendList(String memberId) {
        return friendService
                .getFriends(memberId)
                .map(MemberResponse::fromEntity);
    }

    @Override
    public Mono<Void> breakFriend(String memberId, BreakFriendRequest breakFriendRequest) {
        return friendService
                .breakFriend(memberId, breakFriendRequest.memberId());
    }


}

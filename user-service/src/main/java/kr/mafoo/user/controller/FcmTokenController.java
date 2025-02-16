package kr.mafoo.user.controller;

import kr.mafoo.user.api.FcmTokenApi;
import kr.mafoo.user.controller.dto.request.FcmTokenCreateRequest;
import kr.mafoo.user.controller.dto.request.FcmTokenUpdateRequest;
import kr.mafoo.user.controller.dto.response.FcmTokenResponse;
import kr.mafoo.user.service.FcmTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class FcmTokenController implements FcmTokenApi {

    private final FcmTokenService fcmTokenService;

    @Override
    public Flux<FcmTokenResponse> getFcmTokenList(
        String memberId
    ){
        return fcmTokenService
            .findFcmTokenList()
            .map(FcmTokenResponse::fromEntity);
    }

    @Override
    public Mono<FcmTokenResponse> createFcmToken(
        String memberId,
        FcmTokenCreateRequest request
    ){
        return fcmTokenService
            .addFcmToken(
                memberId,
                request.token()
            )
            .map(FcmTokenResponse::fromEntity);
    }

    @Override
    public Mono<FcmTokenResponse> updateFcmToken(
        String memberId,
        FcmTokenUpdateRequest request
    ){
        return fcmTokenService
            .modifyFcmToken(
                memberId,
                request.token()
            )
            .map(FcmTokenResponse::fromEntity);
    }

    @Override
    public Mono<Void> deleteFcmToken(
        String memberId
    ){
        return fcmTokenService
            .removeFcmToken(
                memberId
            );
    }
}

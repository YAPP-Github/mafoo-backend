package kr.mafoo.user.controller;

import static kr.mafoo.user.enums.NotificationType.NEW_MEMBER;

import java.util.List;
import kr.mafoo.user.api.FcmTokenApi;
import kr.mafoo.user.controller.dto.request.FcmTokenCreateRequest;
import kr.mafoo.user.controller.dto.request.FcmTokenUpdateRequest;
import kr.mafoo.user.controller.dto.response.FcmTokenResponse;
import kr.mafoo.user.service.FcmTokenService;
import kr.mafoo.user.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class FcmTokenController implements FcmTokenApi {

    private final FcmTokenService fcmTokenService;

    private final NotificationService notificationService;

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
            .map(FcmTokenResponse::fromEntity)
            .flatMap(fcmTokenResponse -> notificationService.sendNotificationByScenario(NEW_MEMBER, List.of(memberId), null)
                .then(Mono.just(fcmTokenResponse))
            );
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

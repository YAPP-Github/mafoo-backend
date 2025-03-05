package kr.mafoo.user.service;

import static kr.mafoo.user.enums.NotificationType.NEW_MEMBER;

import java.util.List;
import kr.mafoo.user.domain.FcmTokenEntity;
import kr.mafoo.user.exception.FcmTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FcmTokenService {

    private final FcmTokenQuery fcmTokenQuery;
    private final FcmTokenCommand fcmTokenCommand;

    private final MemberQuery memberQuery;

    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public Flux<FcmTokenEntity> findFcmTokenList() {
        return fcmTokenQuery.findAll()
            .onErrorResume(FcmTokenNotFoundException.class, ex -> Flux.empty());
    }

    @Transactional
    public Mono<FcmTokenEntity> addFcmToken(
        String requestMemberId,
        String token
    ) {
        return memberQuery.findById(requestMemberId)
            .flatMap(member -> fcmTokenQuery.checkDuplicateExists(requestMemberId)
                    .then(fcmTokenCommand.addFcmToken(requestMemberId, token)
                        .flatMap(fcmToken -> notificationService.sendNotificationByScenario(NEW_MEMBER, List.of(requestMemberId), null)
                            .then(Mono.just(fcmToken))
                        )
                    )
            );
    }

    @Transactional
    public Mono<FcmTokenEntity> modifyFcmToken(
        String requestMemberId,
        String token
    ) {
        return fcmTokenQuery.findByOwnerMemberId(requestMemberId)
            .flatMap(fcmToken -> fcmTokenCommand.modifyFcmToken(fcmToken, token));
    }

    @Transactional
    public Mono<Void> removeFcmToken(
        String requestMemberId
    ) {
        return fcmTokenCommand.removeFcmToken(requestMemberId);
    }
}

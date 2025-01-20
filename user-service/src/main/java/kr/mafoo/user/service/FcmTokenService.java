package kr.mafoo.user.service;

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
        return fcmTokenQuery.checkDuplicateExists(requestMemberId)
            .then(fcmTokenCommand.addFcmToken(requestMemberId, token));
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
        return fcmTokenQuery.findByOwnerMemberId(requestMemberId)
            .flatMap(fcmTokenCommand::removeFcmToken);
    }
}

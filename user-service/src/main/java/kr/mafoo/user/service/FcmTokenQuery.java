package kr.mafoo.user.service;

import kr.mafoo.user.domain.FcmTokenEntity;
import kr.mafoo.user.exception.FcmTokenDuplicatedException;
import kr.mafoo.user.exception.FcmTokenNotFoundException;
import kr.mafoo.user.repository.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FcmTokenQuery {

    private final FcmTokenRepository fcmTokenRepository;

    public Flux<FcmTokenEntity> findAll() {
        return fcmTokenRepository.findAll()
            .switchIfEmpty(Mono.error(new FcmTokenNotFoundException()));
    }

    public Mono<FcmTokenEntity> findByOwnerMemberId(String ownerMemberId) {
        return fcmTokenRepository.findByOwnerMemberId(ownerMemberId)
            .switchIfEmpty(Mono.error(new FcmTokenNotFoundException()));
    }

    public Mono<Void> checkDuplicateExists(String ownerMemberId) {
        return fcmTokenRepository.findByOwnerMemberId(ownerMemberId)
            .switchIfEmpty(Mono.empty())
            .flatMap(fcmToken -> Mono.error(new FcmTokenDuplicatedException()));
    }
}

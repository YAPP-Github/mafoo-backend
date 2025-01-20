package kr.mafoo.user.service;

import kr.mafoo.user.domain.FcmTokenEntity;
import kr.mafoo.user.repository.FcmTokenRepository;
import kr.mafoo.user.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FcmTokenCommand {

    private final FcmTokenRepository fcmTokenRepository;

    public Mono<FcmTokenEntity> addFcmToken(String ownerMemberId, String token) {
        return fcmTokenRepository.save(
            FcmTokenEntity.newFcmToken(IdGenerator.generate(), ownerMemberId, token)
        );
    }

    public Mono<FcmTokenEntity> modifyFcmToken(FcmTokenEntity fcmToken, String token) {
        return fcmTokenRepository.save(
            fcmToken.updateToken(token)
        );
    }

    public Mono<Void> removeFcmToken(FcmTokenEntity fcmToken) {
        return fcmTokenRepository.delete(fcmToken);
    }
}

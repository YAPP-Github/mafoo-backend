package kr.mafoo.user.repository;

import kr.mafoo.user.domain.FcmTokenEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface FcmTokenRepository extends R2dbcRepository<FcmTokenEntity, String> {
    Mono<FcmTokenEntity> findByOwnerMemberId(String ownerMemberId);
}

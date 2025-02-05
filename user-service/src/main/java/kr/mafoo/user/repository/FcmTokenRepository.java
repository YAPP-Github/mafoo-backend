package kr.mafoo.user.repository;

import java.util.List;
import kr.mafoo.user.domain.FcmTokenEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FcmTokenRepository extends R2dbcRepository<FcmTokenEntity, String> {
    Mono<FcmTokenEntity> findByOwnerMemberId(String ownerMemberId);

    @Query("SELECT * FROM fcm_token WHERE owner_member_id IN (:ownerMemberIdList)")
    Flux<FcmTokenEntity> findAllByOwnerMemberIdList(List<String> ownerMemberIdList);
}

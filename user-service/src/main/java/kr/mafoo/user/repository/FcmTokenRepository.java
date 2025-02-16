package kr.mafoo.user.repository;

import java.util.List;
import kr.mafoo.user.domain.FcmTokenEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FcmTokenRepository extends R2dbcRepository<FcmTokenEntity, String> {
    Mono<FcmTokenEntity> findByOwnerMemberIdAndDeletedAtNull(String ownerMemberId);

    Flux<FcmTokenEntity> findByDeletedAtNull();

    @Query("SELECT * FROM fcm_token WHERE owner_member_id IN (:ownerMemberIdList)AND deleted_at IS NULL")
    Flux<FcmTokenEntity> findAllByOwnerMemberIdList(List<String> ownerMemberIdList);

    @Modifying
    @Query("UPDATE fcm_token SET deleted_at = NOW() WHERE owner_member_id = :memberId AND deleted_at IS NULL")
    Mono<Void> softDeleteByMemberId(String memberId);
}

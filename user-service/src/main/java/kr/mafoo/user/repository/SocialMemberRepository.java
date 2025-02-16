package kr.mafoo.user.repository;

import kr.mafoo.user.domain.SocialMemberEntity;
import kr.mafoo.user.domain.key.SocialMemberEntityKey;
import kr.mafoo.user.enums.IdentityProvider;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SocialMemberRepository extends R2dbcRepository<SocialMemberEntity, SocialMemberEntityKey> {

    Mono<SocialMemberEntity> findByIdentityProviderAndIdAndDeletedAtIsNull(IdentityProvider identityProvider, String id);

    @Modifying
    @Query("UPDATE social_member SET deleted_at = NOW() WHERE member_id = :memberId AND deleted_at IS NULL")
    Flux<Void> softDeleteByMemberId(String memberId);

}

package kr.mafoo.user.repository;

import kr.mafoo.user.domain.SocialMemberEntity;
import kr.mafoo.user.domain.SocialMemberEntityKey;
import kr.mafoo.user.enums.IdentityProvider;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface SocialMemberRepository extends R2dbcRepository<SocialMemberEntity, SocialMemberEntityKey> {
    Mono<SocialMemberEntity> findByIdentityProviderAndId(IdentityProvider identityProvider, String id);
}

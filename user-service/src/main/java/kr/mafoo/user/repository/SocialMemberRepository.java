package kr.mafoo.user.repository;

import kr.mafoo.user.domain.SocialMemberEntity;
import kr.mafoo.user.domain.SocialMemberEntityKey;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface SocialMemberRepository extends R2dbcRepository<SocialMemberEntity, SocialMemberEntityKey> {

}

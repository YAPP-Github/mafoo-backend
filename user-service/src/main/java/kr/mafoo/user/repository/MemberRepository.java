package kr.mafoo.user.repository;

import kr.mafoo.user.domain.MemberEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MemberRepository extends R2dbcRepository<MemberEntity, String> {
    Flux<MemberEntity> findAllByNameContaining(String memberId);
    Mono<Void> deleteMemberById(String memberId);
}

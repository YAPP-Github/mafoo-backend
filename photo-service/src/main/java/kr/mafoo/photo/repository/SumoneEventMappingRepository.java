package kr.mafoo.photo.repository;

import kr.mafoo.photo.domain.SumoneEventMappingEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface SumoneEventMappingRepository extends R2dbcRepository<SumoneEventMappingEntity, String> {
    Mono<SumoneEventMappingEntity> findByInviteCode(String inviteCode);
}

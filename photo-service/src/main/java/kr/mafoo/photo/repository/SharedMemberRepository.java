package kr.mafoo.photo.repository;

import kr.mafoo.photo.domain.SharedMemberEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SharedMemberRepository extends R2dbcRepository<SharedMemberEntity, String> {
    Flux<SharedMemberEntity> findAllByAlbumId(String albumId);
    Mono<SharedMemberEntity> findByAlbumIdAndMemberId(String albumId, String memberId);
}
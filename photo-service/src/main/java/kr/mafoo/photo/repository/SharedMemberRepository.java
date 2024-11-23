package kr.mafoo.photo.repository;

import kr.mafoo.photo.domain.SharedMemberEntity;
import kr.mafoo.photo.domain.enums.ShareStatus;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SharedMemberRepository extends R2dbcRepository<SharedMemberEntity, String> {
    Flux<SharedMemberEntity> findAllByAlbumIdAndShareStatusNot(String albumId, ShareStatus status);
    Flux<SharedMemberEntity> findAllByMemberIdAndShareStatusNot(String memberId, ShareStatus status);
    Mono<SharedMemberEntity> findByAlbumIdAndMemberIdAndShareStatus(String albumId, String memberId, ShareStatus status);
    Mono<SharedMemberEntity> findByAlbumIdAndMemberId(String albumId, String memberId);
}
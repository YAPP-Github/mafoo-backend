package kr.mafoo.photo.repository;

import java.util.List;
import kr.mafoo.photo.domain.SharedMemberEntity;
import kr.mafoo.photo.domain.enums.ShareStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SharedMemberRepository extends R2dbcRepository<SharedMemberEntity, String> {
    @Query("SELECT * FROM shared_member WHERE album_id IN (:albumIdList)")
    Flux<SharedMemberEntity> findAllByAlbumIdList(List<String> albumIdList);

    @Query("SELECT * FROM shared_member WHERE album_id IN (:albumIdList) AND member_id != :memberId")
    Flux<SharedMemberEntity> findAllByAlbumIdListAndMemberIdNot(List<String> albumIdList, String memberId);

    Flux<SharedMemberEntity> findByMemberId(String memberId);
    Flux<SharedMemberEntity> findAllByAlbumIdAndShareStatusNot(String albumId, ShareStatus status);
    Flux<SharedMemberEntity> findAllByMemberIdAndShareStatusNot(String memberId, ShareStatus status);
    Mono<SharedMemberEntity> findByAlbumIdAndMemberIdAndShareStatus(String albumId, String memberId, ShareStatus status);
    Mono<SharedMemberEntity> findByAlbumIdAndMemberId(String albumId, String memberId);
}
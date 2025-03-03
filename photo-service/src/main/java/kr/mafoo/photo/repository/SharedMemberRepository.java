package kr.mafoo.photo.repository;

import java.util.List;
import kr.mafoo.photo.domain.SharedMemberEntity;
import kr.mafoo.photo.domain.enums.ShareStatus;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SharedMemberRepository extends R2dbcRepository<SharedMemberEntity, String> {
    @Query("SELECT * FROM shared_member WHERE album_id IN (:albumIdList) AND deleted_at IS NULL")
    Flux<SharedMemberEntity> findAllByAlbumIdList(List<String> albumIdList);

    @Query("SELECT * FROM shared_member WHERE album_id IN (:albumIdList) AND member_id != :memberId AND deleted_at IS NULL")
    Flux<SharedMemberEntity> findAllByAlbumIdListAndMemberIdNot(List<String> albumIdList, String memberId);

    Flux<SharedMemberEntity> findByMemberIdAndDeletedAtIsNull(String memberId);

    Mono<SharedMemberEntity> findByIdAndDeletedAtIsNull(String id);

    Mono<SharedMemberEntity> findByAlbumIdAndMemberIdAndShareStatusAndDeletedAtIsNull(String albumId, String memberId, ShareStatus status);

    Mono<SharedMemberEntity> findByAlbumIdAndMemberIdAndDeletedAtIsNull(String albumId, String memberId);

    Flux<SharedMemberEntity> findAllByAlbumIdAndShareStatusAndDeletedAtIsNull(String albumId, ShareStatus status);

    Flux<SharedMemberEntity> findAllByAlbumIdAndShareStatusNotAndDeletedAtIsNull(String albumId, ShareStatus status);

    Flux<SharedMemberEntity> findAllByMemberIdAndShareStatusNotAndDeletedAtIsNull(String memberId, ShareStatus status);

    @Modifying
    @Query("UPDATE shared_member SET deleted_at = NOW() WHERE id = :sharedMemberId AND deleted_at IS NULL")
    Mono<Void> softDeleteById(String sharedMemberId);

    @Modifying
    @Query("UPDATE shared_member SET deleted_at = NOW() WHERE album_id = :albumId AND deleted_at IS NULL")
    Flux<Void> softDeleteByAlbumId(String albumId);

    @Modifying
    @Query("UPDATE shared_member SET deleted_at = NOW() WHERE album_id IN (:albumIds) AND deleted_at IS NULL")
    Flux<Void> softDeleteByAlbumIds(List<String> albumIds);

    @Modifying
    @Query("UPDATE shared_member SET deleted_at = NOW() WHERE member_id = :memberId AND deleted_at IS NULL")
    Flux<Void> softDeleteByMemberId(String memberId);
}
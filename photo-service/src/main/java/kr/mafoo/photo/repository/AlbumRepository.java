package kr.mafoo.photo.repository;

import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.enums.AlbumType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlbumRepository extends R2dbcRepository<AlbumEntity, String> {

    Mono<AlbumEntity> findByAlbumIdAndDeletedAtIsNull(String albumId);

    Flux<AlbumEntity> findAllByOwnerMemberIdAndDeletedAtIsNullOrderByDisplayIndex(String ownerMemberId);

    @Modifying
    @Query("UPDATE album SET deleted_at = NOW() WHERE id = :albumId AND deleted_at IS NULL")
    Mono<Void> softDeleteById(String albumId);

//    @Modifying
//    @Query("UPDATE album SET display_index = display_index + 1 WHERE owner_member_id = :ownerMemberId")
//    Mono<Void> pushDisplayIndex(String ownerMemberId);
//
//    @Modifying
//    @Query("UPDATE album SET display_index = display_index + 1 WHERE owner_member_id = :ownerMemberId " +
//            "AND display_index BETWEEN :startIndex AND :lastIndex")
//    Mono<Void> pushDisplayIndexBetween(String ownerMemberId, Integer startIndex, Integer lastIndex);
//
//    @Modifying
//    @Query("UPDATE album SET display_index = display_index -1 WHERE owner_member_id = :ownerMemberId " +
//            "AND display_index BETWEEN :startIndex AND :lastIndex")
//    Mono<Void> popDisplayIndexBetween(String ownerMemberId, Integer startIndex, Integer lastIndex);

    Flux<AlbumEntity> findAllByOrderByAlbumIdDesc(Pageable pageable);

    Flux<AlbumEntity> findAllByNameOrderByAlbumIdDesc(String name, Pageable pageable);
    Flux<AlbumEntity> findAllByTypeOrderByAlbumIdDesc(AlbumType type, Pageable pageable);

    Flux<AlbumEntity> findAllByOwnerMemberIdOrderByAlbumIdDesc(String ownerMemberId, Pageable pageable);

    Mono<Long> countAlbumEntityByType(AlbumType albumType);

    Flux<AlbumEntity> findAllByExternalId(String externalId);
}

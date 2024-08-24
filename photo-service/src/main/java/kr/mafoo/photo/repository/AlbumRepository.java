package kr.mafoo.photo.repository;

import kr.mafoo.photo.domain.AlbumEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlbumRepository extends R2dbcRepository<AlbumEntity, String> {
    Flux<AlbumEntity> findAllByOwnerMemberIdOrderByDisplayIndex(String ownerMemberId);

    @Modifying
    @Query("UPDATE album SET display_index = display_index + 1 WHERE owner_member_id = :ownerMemberId")
    Mono<Void> pushDisplayIndex(String ownerMemberId);

    @Modifying
    @Query("UPDATE album SET display_index = display_index + 1 WHERE owner_member_id = :ownerMemberId " +
            "AND display_index BETWEEN :startIndex AND :lastIndex")
    Mono<Void> pushDisplayIndexBetween(String ownerMemberId, Integer startIndex, Integer lastIndex);

    @Modifying
    @Query("UPDATE album SET display_index = display_index -1 WHERE owner_member_id = :ownerMemberId " +
            "AND display_index BETWEEN :startIndex AND :lastIndex")
    Mono<Void> popDisplayIndexBetween(String ownerMemberId, Integer startIndex, Integer lastIndex);
}

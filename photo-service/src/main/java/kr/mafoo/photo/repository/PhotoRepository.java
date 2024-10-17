package kr.mafoo.photo.repository;

import kr.mafoo.photo.domain.BrandType;
import kr.mafoo.photo.domain.PhotoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PhotoRepository extends R2dbcRepository<PhotoEntity, String> {
    Flux<PhotoEntity> findAllByAlbumIdOrderByDisplayIndexDesc(String ownerAlbumId);

    Flux<PhotoEntity> findAllByAlbumIdOrderByCreatedAtDesc(String ownerAlbumId);

    Flux<PhotoEntity> findAllByAlbumIdOrderByCreatedAtAsc(String ownerAlbumId);

    @Modifying
    @Query("UPDATE photo SET display_index = display_index - 1 WHERE album_id = :albumId AND display_index > :startIndex")
    Mono<Void> popDisplayIndexGreaterThan(String albumId, int startIndex);

    @Modifying
    @Query("UPDATE photo SET display_index = display_index - 1 WHERE album_id = :albumId AND display_index BETWEEN :startIndex AND :endIndex")
    Mono<Void> popDisplayIndexBetween(String albumId, int startIndex, int endIndex);

    @Modifying
    @Query("UPDATE photo SET display_index = display_index + 1 WHERE album_id = :albumId AND display_index BETWEEN :startIndex AND :endIndex")
    Mono<Void> pushDisplayIndexBetween(String albumId, int startIndex, int endIndex);

    Flux<PhotoEntity> findAllByOrderByPhotoIdDesc(Pageable pageable);
    Flux<PhotoEntity> findAllByBrandOrderByPhotoIdDesc(BrandType brandType, Pageable pageable);
    Flux<PhotoEntity> findAllByOwnerMemberIdOrderByPhotoIdDesc(String ownerMemberId, Pageable pageable);
}

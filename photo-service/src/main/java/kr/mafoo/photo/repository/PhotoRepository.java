package kr.mafoo.photo.repository;

import kr.mafoo.photo.domain.enums.BrandType;
import kr.mafoo.photo.domain.PhotoEntity;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PhotoRepository extends R2dbcRepository<PhotoEntity, String> {

    Mono<PhotoEntity> findByIdAndDeletedAtIsNull(String id);

    Flux<PhotoEntity> findAllByAlbumIdAndDeletedAtIsNullOrderByDisplayIndexDesc(String ownerAlbumId);

    Flux<PhotoEntity> findAllByAlbumIdAndDeletedAtIsNullOrderByCreatedAtDesc(String ownerAlbumId);

    Flux<PhotoEntity> findAllByAlbumIdAndDeletedAtIsNullOrderByCreatedAtAsc(String ownerAlbumId);

    Flux<PhotoEntity> findAllByAlbumIdAndPhotoIdLessThanAndDeletedAtIsNullOrderByPhotoIdDesc(String albumId, String photoId, Limit limit);
    Flux<PhotoEntity> findAllByAlbumIdAndDeletedAtIsNullOrderByPhotoIdDesc(String albumId, Limit limit);

    @Modifying
    @Query("UPDATE photo SET display_index = display_index - 1 WHERE album_id = :albumId AND display_index > :startIndex AND deleted_at IS NULL")
    Flux<Void> updateAllByAlbumIdToDecreaseDisplayIndexGreaterThan(String albumId, int startIndex);

    @Modifying
    @Query("UPDATE photo SET display_index = display_index - 1 WHERE album_id = :albumId AND display_index BETWEEN :startIndex AND :endIndex AND deleted_at IS NULL")
    Flux<Void> updateAllByAlbumIdToDecreaseDisplayIndexBetween(String albumId, int startIndex, int endIndex);

    @Modifying
    @Query("UPDATE photo SET display_index = display_index + 1 WHERE album_id = :albumId AND display_index BETWEEN :startIndex AND :endIndex AND deleted_at IS NULL")
    Flux<Void> updateAllByAlbumIdToIncreaseDisplayIndexBetween(String albumId, int startIndex, int endIndex);

    @Modifying
    @Query("UPDATE photo SET deleted_at = NOW() WHERE id = :photoId AND deleted_at IS NULL")
    Mono<Void> softDeleteById(String photoId);

    @Modifying
    @Query("UPDATE photo SET deleted_at = NOW() WHERE album_id = :albumId AND deleted_at IS NULL")
    Flux<Void> softDeleteByAlbumId(String albumId);

    @Modifying
    @Query("UPDATE photo SET deleted_at = NOW() WHERE owner_member_id = :ownerMemberId AND deleted_at IS NULL")
    Flux<Void> softDeleteByOwnerMemberId(String ownerMemberId);

    Flux<PhotoEntity> findAllByOrderByPhotoIdDesc(Pageable pageable);
    Flux<PhotoEntity> findAllByBrandOrderByPhotoIdDesc(BrandType brandType, Pageable pageable);
    Flux<PhotoEntity> findAllByOwnerMemberIdOrderByPhotoIdDesc(String ownerMemberId, Pageable pageable);
}

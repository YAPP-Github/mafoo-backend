package kr.mafoo.photo.repository;

import kr.mafoo.photo.domain.AlbumExportEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface AlbumExportRepository extends R2dbcRepository<AlbumExportEntity, String> {
    Mono<Boolean> existsByAlbumId(String albumId);
    Mono<AlbumExportEntity> findFirstByAlbumId(String albumId);
    @Modifying
    @Query("UPDATE album_export SET view_count = view_count + 1 WHERE id = :exportId")
    Mono<Void> increaseViewCount(String exportId);
}

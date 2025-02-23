package kr.mafoo.photo.repository;

import kr.mafoo.photo.domain.AlbumExportEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface AlbumExportRepository extends R2dbcRepository<AlbumExportEntity, String> {
    Mono<Boolean> existsByAlbumId(String albumId);
}

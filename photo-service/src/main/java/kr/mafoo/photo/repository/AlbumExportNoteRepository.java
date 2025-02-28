package kr.mafoo.photo.repository;

import kr.mafoo.photo.domain.AlbumExportNoteEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlbumExportNoteRepository extends R2dbcRepository<AlbumExportNoteEntity, String> {
    Flux<AlbumExportNoteEntity> findAllByExportId(String exportId);
    Mono<Long> countByExportId(String exportId);
}

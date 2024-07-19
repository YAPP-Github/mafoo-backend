package kr.mafoo.photo.repository;

import kr.mafoo.photo.domain.PhotoEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PhotoRepository extends R2dbcRepository<PhotoEntity, String> {
    Flux<PhotoEntity> findAllByAlbumId(String ownerAlbumId);
    Mono<Long> countAllByAlbumId(String albumId);
}

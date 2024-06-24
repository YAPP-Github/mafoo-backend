package kr.mafoo.photo.repository;

import kr.mafoo.photo.domain.AlbumEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface AlbumRepository extends R2dbcRepository<AlbumEntity, String> {
    Flux<AlbumEntity> findAllByOwnerMemberId(String ownerMemberId);
}

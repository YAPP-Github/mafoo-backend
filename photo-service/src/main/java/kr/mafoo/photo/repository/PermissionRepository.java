package kr.mafoo.photo.repository;

import kr.mafoo.photo.domain.PermissionEntity;
import kr.mafoo.photo.domain.PermissionType;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PermissionRepository extends R2dbcRepository<PermissionEntity, String> {
    Flux<PermissionEntity> findAllByAlbumId(String albumId);

    Mono<Boolean> existsByAlbumIdAndMemberId(String albumId, String memberId);
    Mono<Boolean> existsByAlbumIdAndMemberIdAndType(String albumId, String memberId, PermissionType type);
}
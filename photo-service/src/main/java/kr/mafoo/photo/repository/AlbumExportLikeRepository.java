package kr.mafoo.photo.repository;

import kr.mafoo.photo.domain.AlbumExportLikeEntity;
import kr.mafoo.photo.domain.key.AlbumExportLikeEntityKey;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface AlbumExportLikeRepository extends R2dbcRepository<AlbumExportLikeEntity, AlbumExportLikeEntityKey> {
    Mono<Boolean> existsByExportIdAndMemberId(String exportId, String memberId);
    Mono<Long> countByExportId(String exportId);
    Mono<Void> deleteByExportIdAndMemberId(String exportId, String memberId);
}

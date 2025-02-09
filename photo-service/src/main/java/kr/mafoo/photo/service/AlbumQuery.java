package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.enums.AlbumType;
import kr.mafoo.photo.exception.AlbumNotFoundException;
import kr.mafoo.photo.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AlbumQuery {

    private final AlbumRepository albumRepository;

    public Mono<AlbumEntity> findById(String albumId) {
        return albumRepository.findByAlbumIdAndDeletedAtIsNull(albumId)
            .switchIfEmpty(Mono.error(new AlbumNotFoundException()));
    }

    public Flux<AlbumEntity> findByMemberId(String memberId) {
        return albumRepository.findAllByOwnerMemberIdAndDeletedAtIsNullOrderByDisplayIndex(memberId)
            .switchIfEmpty(Mono.error(new AlbumNotFoundException()));
    }

    @Cacheable(value = "albumCount", key = "#albumType")
    public Mono<Long> countAlbumByAlbumType(AlbumType albumType) {
        return albumRepository
                .countAlbumEntityByType(albumType)
                .cache();
    }

    @CacheEvict(value = "albumCount", allEntries = true)
    @Scheduled(fixedDelay = 1000 * 10)
    public void clearAlbumCountCache() {
        LoggerFactory.getLogger(AlbumQuery.class).debug("clear album count cache");
    }
}

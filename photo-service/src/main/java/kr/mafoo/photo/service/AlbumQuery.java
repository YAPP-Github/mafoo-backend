package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.exception.AlbumNotFoundException;
import kr.mafoo.photo.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AlbumQuery {

    private final AlbumRepository albumRepository;

    public Mono<AlbumEntity> findById(String albumId) {
        return albumRepository.findById(albumId)
            .switchIfEmpty(Mono.error(new AlbumNotFoundException()));
    }

    public Flux<AlbumEntity> findByMemberId(String memberId) {
        return albumRepository.findAllByOwnerMemberIdOrderByDisplayIndex(memberId)
            .switchIfEmpty(Mono.error(new AlbumNotFoundException()));
    }
}

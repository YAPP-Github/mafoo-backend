package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.PhotoEntity;
import kr.mafoo.photo.exception.PhotoNotFoundException;
import kr.mafoo.photo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class PhotoQuery {

    private final PhotoRepository photoRepository;

    public Flux<PhotoEntity> findAllByAlbumIdOrderByCreatedAtAsc(String albumId) {
        return photoRepository.findAllByAlbumIdOrderByCreatedAtAsc(albumId)
            .switchIfEmpty(Mono.error(new PhotoNotFoundException()));
    }

    public Flux<PhotoEntity> findAllByAlbumIdOrderByCreatedAtDesc(String albumId) {
        return photoRepository.findAllByAlbumIdOrderByCreatedAtDesc(albumId)
            .switchIfEmpty(Mono.error(new PhotoNotFoundException()));
    }

    public Flux<PhotoEntity> findAllByAlbumIdOrderByDisplayIndexDesc(String albumId) {
        return photoRepository.findAllByAlbumIdOrderByDisplayIndexDesc(albumId)
            .switchIfEmpty(Mono.error(new PhotoNotFoundException()));
    }

    public Mono<PhotoEntity> findByPhotoId(String photoId) {
        return photoRepository.findById(photoId)
            .switchIfEmpty(Mono.error(new PhotoNotFoundException()));
    }

}

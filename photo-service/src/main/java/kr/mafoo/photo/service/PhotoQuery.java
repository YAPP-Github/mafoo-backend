package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.PhotoEntity;
import kr.mafoo.photo.exception.PhotoNotFoundException;
import kr.mafoo.photo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class PhotoQuery {

    private final PhotoRepository photoRepository;

    public Flux<PhotoEntity> findAllByAlbumIdOrderByCreatedAtAsc(String albumId) {
        return photoRepository.findAllByAlbumIdAndDeletedAtIsNullOrderByCreatedAtAsc(albumId)
            .switchIfEmpty(Mono.error(new PhotoNotFoundException()));
    }

    public Flux<PhotoEntity> findAllByAlbumIdOrderByCreatedAtDesc(String albumId) {
        return photoRepository.findAllByAlbumIdAndDeletedAtIsNullOrderByCreatedAtDesc(albumId)
            .switchIfEmpty(Mono.error(new PhotoNotFoundException()));
    }

    public Flux<PhotoEntity> findAllByAlbumIdOrderByDisplayIndexDesc(String albumId) {
        return photoRepository.findAllByAlbumIdAndDeletedAtIsNullOrderByDisplayIndexDesc(albumId)
            .switchIfEmpty(Mono.error(new PhotoNotFoundException()));
    }

    public Mono<PhotoEntity> findByPhotoId(String photoId) {
        return photoRepository.findByIdAndDeletedAtIsNull(photoId)
            .switchIfEmpty(Mono.error(new PhotoNotFoundException()));
    }

    public Flux<PhotoEntity> paginate(String albumId, String photoId, int limit) {
        return (photoId != null ?
                photoRepository.findAllByAlbumIdAndPhotoIdLessThanAndDeletedAtIsNullOrderByPhotoIdDesc(albumId, photoId, Limit.of(limit))
                        : photoRepository.findAllByAlbumIdAndDeletedAtIsNullOrderByPhotoIdDesc(albumId, Limit.of(limit)))
            .switchIfEmpty(Mono.error(new PhotoNotFoundException()));
    }
}

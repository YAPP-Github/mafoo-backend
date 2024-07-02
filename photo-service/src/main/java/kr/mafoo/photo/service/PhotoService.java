package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.PhotoEntity;
import kr.mafoo.photo.exception.AlbumNotFoundException;
import kr.mafoo.photo.exception.PhotoNotFoundException;
import kr.mafoo.photo.repository.AlbumRepository;
import kr.mafoo.photo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final AlbumRepository albumRepository;

    public Flux<PhotoEntity> findAllByAlbumId(String albumId, String requestMemberId) {
        return albumRepository
                .findById(albumId)
                .switchIfEmpty(Mono.error(new AlbumNotFoundException()))
                .flatMapMany(albumEntity -> {
                    if(!albumEntity.getOwnerMemberId().equals(requestMemberId)) {
                        // 내 앨범이 아니면 그냥 없는 앨범 처리
                        return Mono.error(new AlbumNotFoundException());
                    } else {
                        return photoRepository.findAllByAlbumId(albumId);
                    }
                });
    }

    public Mono<Void> deletePhotoById(String photoId, String requestMemberId) {
        return photoRepository
                .findById(photoId)
                .switchIfEmpty(Mono.error(new PhotoNotFoundException()))
                .flatMap(photoEntity -> {
                    if(!photoEntity.getOwnerMemberId().equals(requestMemberId)) {
                        // 내 사진이 아니면 그냥 없는 사진 처리
                        return Mono.error(new PhotoNotFoundException());
                    } else {
                        return photoRepository.deleteById(photoId);
                    }
                });
    }

    public Mono<PhotoEntity> updatePhotoAlbumId(String photoId, String albumId, String requestMemberId) {
        return photoRepository
                .findById(photoId)
                .switchIfEmpty(Mono.error(new PhotoNotFoundException()))
                .flatMap(photoEntity -> {
                    if(!photoEntity.getOwnerMemberId().equals(requestMemberId)) {
                        // 내 사진이 아니면 그냥 없는 사진 처리
                        return Mono.error(new PhotoNotFoundException());
                    } else {
                        return albumRepository
                                .findById(albumId)
                                .switchIfEmpty(Mono.error(new AlbumNotFoundException()))
                                .flatMap(albumEntity -> {
                                    if(!albumEntity.getOwnerMemberId().equals(requestMemberId)) {
                                        // 내 앨범이 아니면 그냥 없는 앨범 처리
                                        return Mono.error(new AlbumNotFoundException());
                                    } else {
                                        return photoRepository.save(photoEntity.updateAlbumId(albumId));
                                    }
                                });
                    }
                });
    }

}

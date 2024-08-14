package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.AlbumType;
import kr.mafoo.photo.exception.AlbumIndexIsSameException;
import kr.mafoo.photo.exception.AlbumNotFoundException;
import kr.mafoo.photo.repository.AlbumRepository;
import kr.mafoo.photo.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AlbumService {
    private final AlbumRepository albumRepository;

    @Transactional
    public Mono<AlbumEntity> createNewAlbum(String ownerMemberId, String albumName, AlbumType albumType) {
        AlbumEntity albumEntity = AlbumEntity.newAlbum(IdGenerator.generate(), albumName, albumType, ownerMemberId);
        return albumRepository
                .pushDisplayIndex(ownerMemberId) //전부 인덱스 한칸 밀기
                .then(albumRepository.save(albumEntity));
    }

    @Transactional
    public Mono<AlbumEntity> moveAlbumDisplayIndex(String albumId, String requestMemberId, Long displayIndex) {
        return findByAlbumId(albumId, requestMemberId)
                .flatMap(album -> {
                    Long currentDisplayIndex = album.getDisplayIndex();
                    Mono<Void> pushAlbumIndexPublisher;
                    if(displayIndex < currentDisplayIndex) {
                        pushAlbumIndexPublisher = albumRepository
                                .pushDisplayIndexBetween(requestMemberId, displayIndex, currentDisplayIndex -1);
                    } else if(displayIndex > currentDisplayIndex) {
                        pushAlbumIndexPublisher = albumRepository
                                .popDisplayIndexBetween(requestMemberId, currentDisplayIndex + 1, displayIndex);
                    } else {
                        pushAlbumIndexPublisher = Mono.error(new AlbumIndexIsSameException());
                    }
                    return pushAlbumIndexPublisher.then(Mono.defer(() -> {
                        album.setDisplayIndex(displayIndex);
                        return albumRepository.save(album);
                    }));
                });
    }

    public Flux<AlbumEntity> findAllByOwnerMemberId(String ownerMemberId) {
        return albumRepository.findAllByOwnerMemberId(ownerMemberId);
    }

    public Mono<AlbumEntity> findByAlbumId(String albumId, String requestMemberId) {
        return albumRepository
                .findById(albumId)
                .switchIfEmpty(Mono.error(new AlbumNotFoundException()))
                .flatMap(albumEntity -> {
                    if(!albumEntity.getOwnerMemberId().equals(requestMemberId)) {
                        // 내 앨범이 아니면 그냥 없는 앨범 처리
                        return Mono.error(new AlbumNotFoundException());
                    } else {
                        return Mono.just(albumEntity);
                    }
                });
    }

    @Transactional
    public Mono<Void> deleteAlbumById(String albumId, String requestMemberId) {
        return albumRepository
                .findById(albumId)
                .switchIfEmpty(Mono.error(new AlbumNotFoundException()))
                .flatMap(albumEntity -> {
                    if(!albumEntity.getOwnerMemberId().equals(requestMemberId)) {
                        // 내 앨범이 아니면 그냥 없는 앨범 처리
                        return Mono.error(new AlbumNotFoundException());
                    } else {
                        return albumRepository.deleteById(albumId);
                    }
                });
    }

    @Transactional
    public Mono<AlbumEntity> updateAlbumName(String albumId, String albumName, String requestMemberId) {
        return albumRepository
                .findById(albumId)
                .switchIfEmpty(Mono.error(new AlbumNotFoundException()))
                .flatMap(albumEntity -> {
                    if(!albumEntity.getOwnerMemberId().equals(requestMemberId)) {
                        // 내 앨범이 아니면 그냥 없는 앨범 처리
                        return Mono.error(new AlbumNotFoundException());
                    } else {
                        return albumRepository.save(albumEntity.updateName(albumName));
                    }
                });
    }

    @Transactional
    public Mono<AlbumEntity> updateAlbumType(String albumId, AlbumType albumType, String requestMemberId) {
        return albumRepository
                .findById(albumId)
                .switchIfEmpty(Mono.error(new AlbumNotFoundException()))
                .flatMap(albumEntity -> {
                    if(!albumEntity.getOwnerMemberId().equals(requestMemberId)) {
                        // 내 앨범이 아니면 그냥 없는 앨범 처리
                        return Mono.error(new AlbumNotFoundException());
                    } else {
                        return albumRepository.save(albumEntity.updateType(albumType));
                    }
                });
    }

    @Transactional
    public Mono<Void> increaseAlbumPhotoCount(String albumId, String requestMemberId) {
        return albumRepository
                .findById(albumId)
                .switchIfEmpty(Mono.error(new AlbumNotFoundException()))
                .flatMap(albumEntity -> {
                    if (!albumEntity.getOwnerMemberId().equals(requestMemberId)) {
                        // 내 앨범이 아니면 그냥 없는 앨범 처리
                        return Mono.error(new AlbumNotFoundException());
                    } else {
                        return albumRepository.save(albumEntity.increasePhotoCount()).then();
                    }
                });
    }

    @Transactional
    public Mono<Void> decreaseAlbumPhotoCount(String albumId, String requestMemberId) {

        if (albumId == null) {
            return Mono.empty();
        }

        return albumRepository
                .findById(albumId)
                .switchIfEmpty(Mono.error(new AlbumNotFoundException()))
                .flatMap(albumEntity -> {
                    if (!albumEntity.getOwnerMemberId().equals(requestMemberId)) {
                        // 내 앨범이 아니면 그냥 없는 앨범 처리
                        return Mono.error(new AlbumNotFoundException());
                    } else {
                        return albumRepository.save(albumEntity.decreasePhotoCount()).then();
                    }
                });
    }

}

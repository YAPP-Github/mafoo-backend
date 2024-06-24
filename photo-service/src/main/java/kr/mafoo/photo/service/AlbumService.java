package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.AlbumType;
import kr.mafoo.photo.repository.AlbumRepository;
import kr.mafoo.photo.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AlbumService {
    private final AlbumRepository albumRepository;

    public Mono<AlbumEntity> createNewAlbum(String ownerMemberId, String albumName, AlbumType albumType) {
        AlbumEntity albumEntity = AlbumEntity.newAlbum(IdGenerator.generate(), albumName, albumType, ownerMemberId);
        return albumRepository.save(albumEntity);
    }

    public Flux<AlbumEntity> findAllByOwnerMemberId(String ownerMemberId) {
        return albumRepository.findAllByOwnerMemberId(ownerMemberId);
    }

    public Mono<Void> deleteAlbumById(String albumId) {
        return albumRepository.deleteById(albumId);
    }

    public Mono<AlbumEntity> updateAlbumName(String albumId, String albumName) {
        return albumRepository
                .findById(albumId)
                .map(albumEntity -> albumEntity.updateName(albumName))
                .flatMap(albumRepository::save);
    }

    public Mono<AlbumEntity> updateAlbumType(String albumId, AlbumType albumType) {
        return albumRepository
                .findById(albumId)
                .map(albumEntity -> albumEntity.updateType(albumType))
                .flatMap(albumRepository::save);
    }
}

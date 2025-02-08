package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.enums.AlbumType;
import kr.mafoo.photo.repository.AlbumRepository;
import kr.mafoo.photo.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AlbumCommand {

    private final AlbumRepository albumRepository;

    public Mono<AlbumEntity> addAlbum(String albumName, String albumType, String ownerMemberId, String externalId) {
        return albumRepository.save(
            AlbumEntity.newAlbum(IdGenerator.generate(), albumName, AlbumType.valueOf(albumType), ownerMemberId, externalId)
        );
    }

    public Mono<AlbumEntity> modifyAlbumNameAndType(AlbumEntity album, String newAlbumName, String newAlbumType) {
        return albumRepository.save(album
            .updateName(newAlbumName)
            .updateType(AlbumType.valueOf(newAlbumType))
        );
    }

    public Mono<AlbumEntity> modifyAlbumOwnership(AlbumEntity album, String newOwnerMemberId) {
        return albumRepository.save(album.updateOwnerMemberId(newOwnerMemberId));
    }

    public Mono<AlbumEntity> increaseAlbumPhotoCount(AlbumEntity album, int count) {
        return albumRepository.save(album.increasePhotoCount(count));
    }

    public Mono<AlbumEntity> decreaseAlbumPhotoCount(AlbumEntity album, int count) {
        return albumRepository.save(album.decreasePhotoCount(count));
    }

    public Mono<Void> removeAlbum(String albumId) {
        return albumRepository.softDeleteById(albumId);
    }

}

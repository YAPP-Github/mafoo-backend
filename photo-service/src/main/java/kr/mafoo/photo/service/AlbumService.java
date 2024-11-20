package kr.mafoo.photo.service;

import static kr.mafoo.photo.domain.enums.PermissionLevel.FULL_ACCESS;
import static kr.mafoo.photo.domain.enums.PermissionLevel.VIEW_ACCESS;

import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.enums.AlbumType;
import kr.mafoo.photo.exception.AlbumIndexIsSameException;
import kr.mafoo.photo.exception.AlbumNotFoundException;
import kr.mafoo.photo.exception.PermissionNotAllowedException;
import kr.mafoo.photo.repository.AlbumRepository;
import kr.mafoo.photo.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static kr.mafoo.photo.domain.PermissionType.FULL_ACCESS;

@RequiredArgsConstructor
@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final PermissionService permissionService;

    @Transactional
    public Mono<AlbumEntity> createNewAlbum(String ownerMemberId, String albumName, AlbumType albumType) {
        AlbumEntity albumEntity = AlbumEntity.newAlbum(IdGenerator.generate(), albumName, albumType, ownerMemberId);
        return albumRepository
                .pushDisplayIndex(ownerMemberId) //전부 인덱스 한칸 밀기
                .then(albumRepository.save(albumEntity));
    }

    @Transactional
    public Mono<AlbumEntity> moveAlbumDisplayIndex(String albumId, String requestMemberId, Integer displayIndex) {
        return findByAlbumId(albumId, requestMemberId)
                .flatMap(album -> {
                    Integer currentDisplayIndex = album.getDisplayIndex();
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
        return albumRepository.findAllByOwnerMemberIdOrderByDisplayIndex(ownerMemberId);
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
        return findByAlbumId(albumId, requestMemberId)
                .flatMap(albumEntity ->
                        albumRepository
                                .deleteById(albumId)
                                .then(albumRepository.popDisplayIndexBetween(
                                        requestMemberId, albumEntity.getDisplayIndex(), Integer.MAX_VALUE))
                );
    }

    @Transactional
    public Mono<AlbumEntity> updateAlbumName(String albumId, String albumName, String requestMemberId) {
        return findByAlbumId(albumId, requestMemberId)
                .flatMap(albumEntity -> albumRepository.save(albumEntity.updateName(albumName)));
    }

    @Transactional
    public Mono<AlbumEntity> updateAlbumType(String albumId, AlbumType albumType, String requestMemberId) {
        return findByAlbumId(albumId, requestMemberId)
                .flatMap(albumEntity -> albumRepository.save(albumEntity.updateType(albumType)));
    }

    @Transactional
//     public Mono<Void> increaseAlbumPhotoCount(String albumId) {
//         return albumRepository
//                 .findById(albumId)
//                 .switchIfEmpty(Mono.error(new AlbumNotFoundException()))
//                 .flatMap(albumEntity -> albumRepository.save(albumEntity.increasePhotoCount()).then());
//     }

//     @Transactional
//     public Mono<Void> decreaseAlbumPhotoCount(String albumId) {

//         if (albumId == null) {
//             return Mono.empty();
//         }

//         return albumRepository
//                 .findById(albumId)
//                 .switchIfEmpty(Mono.error(new AlbumNotFoundException()))
//                 .flatMap(albumEntity -> albumRepository.save(albumEntity.decreasePhotoCount()).then());
//     }

//     public Mono<AlbumEntity> checkAlbumFullAccessPermission(String albumId, String requestMemberId) {
//         return albumRepository.findById(albumId)
//                 .switchIfEmpty(Mono.error(new AlbumNotFoundException()))
//                 .flatMap(albumEntity -> {
//                     if (!albumEntity.getOwnerMemberId().equals(requestMemberId)) {
//                         return permissionService.checkPermissionExistsByType(albumId, requestMemberId, FULL_ACCESS)
//                                 .flatMap(isPermitted -> {
//                                     if (!isPermitted) {
//                                         return Mono.error(new PermissionNotAllowedException());
//                                     } else {
//                                         return Mono.just(albumEntity);
//                                     }
//                                 });
//                     } else {
//                         return Mono.just(albumEntity);
//                     }
//                 });
//     }

//     public Mono<AlbumEntity> checkAlbumReadPermission(String albumId, String requestMemberId) {
//         return albumRepository.findById(albumId)
//                 .switchIfEmpty(Mono.error(new AlbumNotFoundException()))
//                 .flatMap(albumEntity -> {
//                     if (!albumEntity.getOwnerMemberId().equals(requestMemberId)) {
//                         return permissionService.checkPermissionExists(albumId, requestMemberId)
//                                 .flatMap(isPermitted -> {
//                                     if (!isPermitted) {
//                                         return Mono.error(new PermissionNotAllowedException());
//                                     } else {
//                                         return Mono.just(albumEntity);
//                                     }
//                                 });
//                     } else {
//                         return Mono.just(albumEntity);
//                     }
//                 });

    @Transactional
    public Mono<AlbumEntity> increaseAlbumPhotoCount(String albumId, int count, String requestMemberId) {
        return findByAlbumId(albumId, requestMemberId)
                .flatMap(albumEntity -> albumRepository.save(albumEntity.increasePhotoCount(count)));
    }

    @Transactional
    public Mono<AlbumEntity> decreaseAlbumPhotoCount(String albumId, int count, String requestMemberId) {
        return Mono.justOrEmpty(albumId)
                .switchIfEmpty(Mono.empty())
                .flatMap(id -> findByAlbumId(id, requestMemberId))
                .flatMap(albumEntity -> albumRepository.save(albumEntity.decreasePhotoCount(count)));

    }

}

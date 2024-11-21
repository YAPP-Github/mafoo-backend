package kr.mafoo.photo.service;

import static kr.mafoo.photo.domain.enums.PermissionLevel.FULL_ACCESS;
import static kr.mafoo.photo.domain.enums.PermissionLevel.VIEW_ACCESS;

import kr.mafoo.photo.domain.AlbumEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AlbumService {

    private final AlbumQuery albumQuery;
    private final AlbumCommand albumCommand;

    private final AlbumPermissionQuery albumPermissionQuery;

    @Transactional(readOnly = true)
    public Flux<AlbumEntity> findAlbumListByMemberId(String memberId) {
        return null;
    }

    @Transactional(readOnly = true)
    public Mono<AlbumEntity> findAlbumById(String albumId, String memberId) {
        return albumPermissionQuery.verifyOwnershipOrAccessPermission(albumId, memberId, VIEW_ACCESS)
            .then(albumQuery.findById(albumId));
    }

    @Transactional
    public Mono<AlbumEntity> addAlbum(String albumName, String albumType, String requestMemberId) {
        return albumCommand.addAlbum(albumName, albumType, requestMemberId);
    }

    @Transactional
    public Mono<AlbumEntity> modifyAlbumNameAndType(String albumId, String newAlbumName, String newAlbumType, String requestMemberId) {
        return albumPermissionQuery.verifyOwnershipOrAccessPermission(albumId, requestMemberId, FULL_ACCESS)
            .flatMap(album -> albumCommand.modifyAlbumNameAndType(album, newAlbumName, newAlbumType));
    }

    @Transactional
    public Mono<AlbumEntity> modifyAlbumOwnership(String albumId, String newOwnerMemberId, String requestMemberId) {
        return albumPermissionQuery.verifyOwnership(albumId, requestMemberId)
            .flatMap(album -> albumCommand.modifyAlbumOwnership(album, newOwnerMemberId)
                // TODO : 앨범 내부 사진 소유자를 새로운 앨범 소유자로 변경
            );
    }

    @Transactional
    public Mono<Void> removeAlbum(String albumId, String requestMemberId) {
        return albumPermissionQuery.verifyOwnership(albumId, requestMemberId)
            .flatMap(albumCommand::removeAlbum);
    }

}
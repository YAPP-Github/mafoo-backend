package kr.mafoo.photo.service;

import kr.mafoo.photo.controller.dto.request.PermissionCreateRequest;
import kr.mafoo.photo.domain.PermissionEntity;
import kr.mafoo.photo.domain.PermissionType;
import kr.mafoo.photo.exception.AlbumNotFoundException;
import kr.mafoo.photo.exception.CannotMakePermissionMyselfException;
import kr.mafoo.photo.exception.PermissionAlreadyExistsException;
import kr.mafoo.photo.exception.PermissionNotFoundException;
import kr.mafoo.photo.repository.AlbumRepository;
import kr.mafoo.photo.repository.PermissionRepository;
import kr.mafoo.photo.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PermissionService {
    private final AlbumRepository albumRepository;
    private final PermissionRepository permissionRepository;

    @Transactional
    public Flux<PermissionEntity> createNewPermissionBulk(List<PermissionCreateRequest> permissions, String albumId, String requestMemberId) {
        return albumRepository
                .findById(albumId)
                .switchIfEmpty(Mono.error(new PermissionNotFoundException()))
                .flatMapMany(albumEntity -> {
                    if (!albumEntity.getOwnerMemberId().equals(requestMemberId)) {
                        // 내 앨범이 아니면 그냥 없는 앨범 처리
                        return Mono.error(new AlbumNotFoundException());
                    } else {
                        return Flux.fromIterable(permissions)
                                .flatMap(permission ->
                                    this.createNewPermission(albumId, permission.type(), permission.memberId(), requestMemberId)
                                );
                    }
                });
    }

    private Mono<PermissionEntity> createNewPermission(String albumId, String permissionType, String permissionMemberId, String requestMemberId) {
        if (permissionMemberId.equals(requestMemberId)) {
            return Mono.error(new CannotMakePermissionMyselfException());
        }

        return checkPermissionExists(albumId, permissionMemberId)
                .flatMap(permissionExists -> {
                    if (!permissionExists) {
                        return permissionRepository.save(PermissionEntity.newPermission(IdGenerator.generate(), PermissionType.valueOf(permissionType), permissionMemberId, albumId));
                    } else {
                        return Mono.error(new PermissionAlreadyExistsException());
                    }
                });
    }

    @Transactional(readOnly = true)
    public Flux<PermissionEntity> findAllByAlbumId(String albumId, String requestMemberId) {
        return albumRepository
                .findById(albumId)
                .switchIfEmpty(Mono.error(new PermissionNotFoundException()))
                .flatMapMany(albumEntity -> {
                    if (!albumEntity.getOwnerMemberId().equals(requestMemberId)) {
                        // 내 앨범이 아니면 그냥 없는 앨범 처리
                        return Mono.error(new AlbumNotFoundException());
                    } else {
                        return permissionRepository.findAllByAlbumId(albumId);
                    }
                });
    }

    @Transactional
    public Mono<Void> deletePermissionById(String permissionId, String requestMemberId) {
        return permissionRepository
                .findById(permissionId)
                .switchIfEmpty(Mono.error(new PermissionNotFoundException()))
                .flatMap(permissionEntity -> albumRepository
                        .findById(permissionEntity.getAlbumId())
                        .flatMap(albumEntity -> {
                            if (!albumEntity.getOwnerMemberId().equals(requestMemberId)) {
                                return Mono.error(new PermissionNotFoundException());
                            }

                            return permissionRepository.deleteById(permissionId);
                        })
                );

    }

    @Transactional
    public Mono<PermissionEntity> updatePermissionType(String permissionId, String permissionType, String requestMemberId) {
        return permissionRepository
                .findById(permissionId)
                .switchIfEmpty(Mono.error(new PermissionNotFoundException()))
                .flatMap(permissionEntity -> albumRepository
                        .findById(permissionEntity.getAlbumId())
                        .switchIfEmpty(Mono.error(new AlbumNotFoundException()))
                        .flatMap(albumEntity -> {
                            if (!albumEntity.getOwnerMemberId().equals(requestMemberId)) {
                                return Mono.error(new PermissionNotFoundException());
                            }

                            return permissionRepository.save(permissionEntity.updateType(permissionType));
                        })
                );
    }

    public Mono<Boolean> checkPermissionExists(String albumId, String requestMemberId) {
        return permissionRepository.existsByAlbumIdAndMemberId(albumId, requestMemberId);
    }

    public Mono<Boolean> checkPermissionExistsByType(String albumId, String requestMemberId, PermissionType type) {
        return permissionRepository.existsByAlbumIdAndMemberIdAndType(albumId, requestMemberId, type);
    }

}

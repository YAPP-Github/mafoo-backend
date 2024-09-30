package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.PermissionApi;
import kr.mafoo.photo.controller.dto.request.*;
import kr.mafoo.photo.controller.dto.response.PermissionResponse;
import kr.mafoo.photo.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class PermissionController implements PermissionApi {

    private final PermissionService permissionService;

    @Override
    public Flux<PermissionResponse> getPermissions(
            String memberId,
            String albumId
    ){
        return permissionService
                .findAllByAlbumId(albumId, memberId)
                .map(PermissionResponse::fromEntity);
    }

    @Override
    public Flux<PermissionResponse> createPermissionBulk(
            String memberId,
            PermissionBulkCreateRequest request
    ){
        return permissionService
                .createNewPermissionBulk(request.permissions(), request.albumId(), memberId)
                .map(PermissionResponse::fromEntity);
    }

    @Override
    public Mono<PermissionResponse> updatePermissionType(
            String memberId,
            String permissionId,
            PermissionTypeUpdateRequest request
    ){
        return permissionService
                .updatePermissionType(permissionId, request.type(), memberId)
                .map(PermissionResponse::fromEntity);
    }

    @Override
    public Mono<Void> deletePermission(
            String memberId,
            String permissionId
    ){
        return permissionService
                .deletePermissionById(permissionId, memberId);
    }
}

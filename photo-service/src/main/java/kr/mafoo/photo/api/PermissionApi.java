package kr.mafoo.photo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.mafoo.photo.annotation.RequestMemberId;
import kr.mafoo.photo.annotation.ULID;
import kr.mafoo.photo.controller.dto.request.PermissionBulkCreateRequest;
import kr.mafoo.photo.controller.dto.request.PermissionTypeUpdateRequest;
import kr.mafoo.photo.controller.dto.response.PermissionResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@Tag(name = "권한 관련 API", description = "권한 조회, 생성, 수정, 삭제 등 API")
@RequestMapping("/v1/permissions")
public interface PermissionApi {
    @Operation(summary = "권한 조회", description = "권한 목록을 조회합니다.")
    @GetMapping
    Flux<PermissionResponse> getPermissions(
            @RequestMemberId
            String memberId,

            @ULID
            @Parameter(description = "앨범 ID", example = "test_album_id")
            @RequestParam
            String albumId
    );

    @Operation(summary = "권한 n건 생성", description = "권한 여러 개를 생성합니다.")
    @PostMapping("/bulk")
    Flux<PermissionResponse> createPermissionBulk(
            @RequestMemberId
            String memberId,

            @Valid
            @RequestBody
            PermissionBulkCreateRequest request
    );

    @Operation(summary = "권한 종류 수정", description = "권한의 종류를 수정합니다.")
    @PatchMapping("/{permissionId}/type")
    Mono<PermissionResponse> updatePermissionType(
            @RequestMemberId
            String memberId,

            @ULID
            @Parameter(description = "권한 ID", example = "test_permission_id")
            @PathVariable
            String permissionId,

            @Valid
            @RequestBody
            PermissionTypeUpdateRequest request
    );

    @Operation(summary = "권한 삭제", description = "권한을 삭제합니다.")
    @DeleteMapping("{permissionId}")
    Mono<Void> deletePermission(
            @RequestMemberId
            String memberId,

            @ULID
            @Parameter(description = "권한 ID", example = "test_permission_id")
            @PathVariable
            String permissionId
    );
}

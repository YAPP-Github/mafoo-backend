package kr.mafoo.photo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.mafoo.photo.annotation.RequestMemberId;
import kr.mafoo.photo.annotation.ULID;
import kr.mafoo.photo.controller.dto.request.SharedMemberCreateRequest;
import kr.mafoo.photo.controller.dto.request.SharedMemberUpdatePermissionRequest;
import kr.mafoo.photo.controller.dto.request.SharedMemberUpdateStatusRequest;
import kr.mafoo.photo.controller.dto.response.SharedMemberDetailResponse;
import kr.mafoo.photo.controller.dto.response.SharedMemberResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@Tag(name = "공유 사용자 관련 API", description = "공유 사용자 조회, 생성, 수정, 삭제 등 API")
@RequestMapping("/v1/shared-members")
public interface SharedMemberApi {
    @Operation(summary = "공유 사용자 조회", description = "앨범 별 공유 사용자 목록을 조회합니다.")
    @GetMapping
    Flux<SharedMemberDetailResponse> getSharedMemberListByAlbum(
            @RequestMemberId
            String memberId,

            @ULID
            @Parameter(description = "앨범 ID", example = "test_album_id")
            @RequestParam
            String albumId
    );

    @Operation(summary = "공유 사용자 생성", description = "공유 사용자를 생성합니다.")
    @PostMapping
    Mono<SharedMemberResponse> createSharedMember(
            @RequestMemberId
            String memberId,

            @Valid
            @RequestBody
            SharedMemberCreateRequest request
    );

    @Operation(summary = "공유 사용자 상태 수정", description = "공유 사용자의 상태를 수정합니다.")
    @PatchMapping("/{sharedMemberId}/status")
    Mono<SharedMemberResponse> updateSharedMemberStatus(
        @RequestMemberId
        String memberId,

        @ULID
        @Parameter(description = "공유 사용자 ID", example = "test_shared_member_id")
        @PathVariable
        String sharedMemberId,

        @Valid
        @RequestBody
        SharedMemberUpdateStatusRequest request
    );

    @Operation(summary = "공유 사용자 권한 수정", description = "공유 사용자의 권한을 수정합니다.")
    @PatchMapping("/{sharedMemberId}/permission")
    Mono<SharedMemberResponse> updateSharedMemberPermission(
            @RequestMemberId
            String memberId,

            @ULID
            @Parameter(description = "공유 사용자 ID", example = "test_shared_member_id")
            @PathVariable
            String sharedMemberId,

            @Valid
            @RequestBody
            SharedMemberUpdatePermissionRequest request
    );

    @Operation(summary = "공유 사용자 삭제", description = "공유 사용자를 삭제합니다.")
    @DeleteMapping("{sharedMemberId}")
    Mono<Void> deleteSharedMember(
            @RequestMemberId
            String memberId,

            @ULID
            @Parameter(description = "공유 사용자 ID", example = "test_shared_member_id")
            @PathVariable
            String sharedMemberId
    );
}

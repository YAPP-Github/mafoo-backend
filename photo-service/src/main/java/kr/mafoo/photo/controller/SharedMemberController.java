package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.SharedMemberApi;
import kr.mafoo.photo.controller.dto.request.SharedMemberCreateRequest;
import kr.mafoo.photo.controller.dto.request.SharedMemberUpdatePermissionRequest;
import kr.mafoo.photo.controller.dto.request.SharedMemberUpdateStatusRequest;
import kr.mafoo.photo.controller.dto.response.SharedMemberResponse;
import kr.mafoo.photo.service.SharedMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class SharedMemberController implements SharedMemberApi {

    private final SharedMemberService sharedMemberService;

    @Override
    public Mono<SharedMemberResponse> createSharedMember(
        String memberId,
        SharedMemberCreateRequest request
    ){
        return sharedMemberService.addSharedMember(request.albumId(), request.permissionLevel(), request.memberId(), memberId)
            .map(SharedMemberResponse::fromEntity);
    }

    @Override
    public Mono<SharedMemberResponse> updateSharedMemberStatus(
        String memberId,
        String sharedMemberId,
        SharedMemberUpdateStatusRequest request
    ){
        return sharedMemberService.modifySharedMemberShareStatus(sharedMemberId, request.shareStatus(), memberId)
            .map(SharedMemberResponse::fromEntity);
    }

    @Override
    public Mono<SharedMemberResponse> updateSharedMemberPermission(
        String memberId,
        String sharedMemberId,
        SharedMemberUpdatePermissionRequest request
    ){
        return sharedMemberService.modifySharedMemberPermissionLevel(sharedMemberId, request.permissionLevel(), memberId)
            .map(SharedMemberResponse::fromEntity);
    }

    @Override
    public Mono<Void> deleteSharedMember(
        String memberId,
        String sharedMemberId
    ){
        return sharedMemberService.removeSharedMember(memberId, sharedMemberId);
    }
}

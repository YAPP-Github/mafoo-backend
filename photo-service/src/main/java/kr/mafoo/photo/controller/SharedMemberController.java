package kr.mafoo.photo.controller;

import java.util.Arrays;
import kr.mafoo.photo.api.SharedMemberApi;
import kr.mafoo.photo.controller.dto.request.SharedMemberCreateRequest;
import kr.mafoo.photo.controller.dto.request.SharedMemberUpdatePermissionRequest;
import kr.mafoo.photo.controller.dto.request.SharedMemberUpdateStatusRequest;
import kr.mafoo.photo.controller.dto.response.SharedMemberDetailResponse;
import kr.mafoo.photo.controller.dto.response.SharedMemberResponse;
import kr.mafoo.photo.domain.enums.PermissionLevel;
import kr.mafoo.photo.domain.enums.ShareStatus;
import kr.mafoo.photo.domain.enums.SharedMemberSortType;
import kr.mafoo.photo.domain.enums.SortOrder;
import kr.mafoo.photo.service.SharedMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class SharedMemberController implements SharedMemberApi {

    private final SharedMemberService sharedMemberService;

    @Override
    public Mono<SharedMemberDetailResponse> getSharedMemberVariablesInOwnedAlbum(
        ShareStatus shareStatus,
        PermissionLevel permissionLevel,
        SharedMemberSortType sort,
        SortOrder order,
        String memberId
    ) {
        return sharedMemberService.findSharedMemberVariablesInOwnedAlbum(shareStatus, permissionLevel, sort, order, memberId)
            .map(SharedMemberDetailResponse::fromDto);
    }

    @Override
    public Mono<SharedMemberDetailResponse> getSharedMemberVariablesInSharedAlbum(
        ShareStatus shareStatus,
        PermissionLevel permissionLevel,
        SharedMemberSortType sort,
        SortOrder order,
        String memberId
    ) {
        return sharedMemberService.findSharedMemberVariablesInSharedAlbum(shareStatus, permissionLevel, sort, order, memberId)
            .map(SharedMemberDetailResponse::fromDto);
    }

    @Override
    public Flux<SharedMemberResponse> getSharedMemberByAlbumAndMemberList(
        String albumId,
        String memberIdList
    ) {
        return sharedMemberService.findSharedMemberByAlbumIdAndMemberIdList(albumId, Arrays.stream(memberIdList.split(",")).toList())
            .map(SharedMemberResponse::fromEntity);
    }

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
    public Mono<SharedMemberResponse> updateSharedMemberStatusAccept(
        String memberId,
        String sharedMemberId
    ){
        return sharedMemberService.modifySharedMemberShareStatusAccept(sharedMemberId, memberId)
            .map(SharedMemberResponse::fromEntity);
    }

    @Override
    public Mono<SharedMemberResponse> updateSharedMemberStatusReject(
        String memberId,
        String sharedMemberId
    ){
        return sharedMemberService.modifySharedMemberShareStatusReject(sharedMemberId, memberId)
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
        return sharedMemberService.removeSharedMember(sharedMemberId, memberId);
    }
}

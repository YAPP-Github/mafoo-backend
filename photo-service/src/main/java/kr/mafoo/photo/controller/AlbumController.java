package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.AlbumApi;
import kr.mafoo.photo.controller.dto.request.AlbumCreateRequest;
import kr.mafoo.photo.controller.dto.request.AlbumUpdateNameAndTypeRequest;
import kr.mafoo.photo.controller.dto.request.AlbumUpdateOwnershipRequest;
import kr.mafoo.photo.controller.dto.response.ViewableAlbumResponse;
import kr.mafoo.photo.controller.dto.response.AlbumResponse;
import kr.mafoo.photo.controller.dto.response.ViewableAlbumDetailResponse;
import kr.mafoo.photo.domain.enums.AlbumSortType;
import kr.mafoo.photo.domain.enums.AlbumType;
import kr.mafoo.photo.domain.enums.SortOrder;
import kr.mafoo.photo.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class AlbumController implements AlbumApi {

    private final AlbumService albumService;

    @Override
    public Flux<ViewableAlbumResponse> getAlbumListByMember(
            String memberId
    ) {
        return albumService
                .findViewableAlbumListByMemberId(memberId)
                .map(ViewableAlbumResponse::fromDto);
    }

    @Override
    public Mono<ViewableAlbumResponse> getAlbumVariablesByTypeAndSort(
        AlbumType type, AlbumSortType sort, SortOrder order, String memberId) {
        return albumService
            .findViewableAlbumByTypeAndSort(type, sort, order, memberId)
            .map(ViewableAlbumResponse::fromDto);
    }

    @Override
    public Mono<ViewableAlbumDetailResponse> getAlbum(
            String memberId,
            String albumId
    ) {
        return albumService
                .findViewableAlbumDetailById(albumId, memberId)
                .map(ViewableAlbumDetailResponse::fromDto);
    }

    @Override
    public Mono<AlbumResponse> createAlbum(
            String memberId,
            AlbumCreateRequest request
    ){
        if (request.type().equals("SUMONE")) {
            throw new RuntimeException(); // should not be happened
        }
        if(request.sumoneInviteCode() != null) {
            return albumService
                    .addSumoneAlbum(request.name(), request.type(), memberId, request.sumoneInviteCode())
                    .map(AlbumResponse::fromEntity);
        }
        return albumService
                .addAlbum(request.name(), request.type(), memberId)
                .map(AlbumResponse::fromEntity);
    }

    // tmp. deprecated
//    @Override
//    public Mono<AlbumResponse> updateAlbumDisplayIndex(
//            String memberId,
//            String albumId,
//            AlbumUpdateDisplayIndexRequest request
//    ) {
//        return Mono.empty();
//    }

    @Override
    public Mono<AlbumResponse> updateAlbumNameAndType(
            String memberId,
            String albumId,
            AlbumUpdateNameAndTypeRequest request
    ) {
        return albumService
                .modifyAlbumNameAndType(albumId, request.name(), request.type(), memberId)
                .map(AlbumResponse::fromEntity);
    }

    @Override
    public Mono<AlbumResponse> updateAlbumOwnerShip(
            String memberId,
            String albumId,
            AlbumUpdateOwnershipRequest request
    ) {
        return albumService
            .modifyAlbumOwnership(albumId, request.newOwnerMemberId(), memberId)
            .map(AlbumResponse::fromEntity);
    }

    @Override
    public Mono<Void> deleteAlbum(
            String memberId,
            String albumId
    ){
        return albumService
                .removeAlbum(albumId, memberId);
    }

}

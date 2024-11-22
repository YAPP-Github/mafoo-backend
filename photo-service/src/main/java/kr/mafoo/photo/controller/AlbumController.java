package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.AlbumApi;
import kr.mafoo.photo.controller.dto.request.AlbumCreateRequest;
import kr.mafoo.photo.controller.dto.request.AlbumUpdateNameAndTypeRequest;
import kr.mafoo.photo.controller.dto.request.AlbumUpdateOwnershipRequest;
import kr.mafoo.photo.controller.dto.response.AlbumDetailResponse;
import kr.mafoo.photo.controller.dto.response.AlbumResponse;
import kr.mafoo.photo.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class AlbumController implements AlbumApi {

    private final AlbumService albumService;

    @Override
    public Flux<AlbumDetailResponse> getAlbumListByMember(
            String memberId,
            ServerHttpRequest serverHttpRequest
    ) {
        String authorizationToken = serverHttpRequest.getHeaders().getFirst("Authorization");

        return albumService
                .findAlbumListByMemberId(memberId, authorizationToken)
                .map(AlbumDetailResponse::fromDto);
    }

    @Override
    public Mono<AlbumResponse> getAlbum(
            String memberId,
            String albumId
    ) {
        return albumService
                .findAlbumById(albumId, memberId)
                .map(AlbumResponse::fromEntity);
    }

    @Override
    public Mono<AlbumResponse> createAlbum(
            String memberId,
            AlbumCreateRequest request
    ){
        return albumService
                .addAlbum(memberId, request.name(), request.type())
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

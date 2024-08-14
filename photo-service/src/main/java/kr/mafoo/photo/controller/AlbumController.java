package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.AlbumApi;
import kr.mafoo.photo.controller.dto.request.AlbumCreateRequest;
import kr.mafoo.photo.controller.dto.request.AlbumUpdateDisplayIndexRequest;
import kr.mafoo.photo.controller.dto.request.AlbumUpdateRequest;
import kr.mafoo.photo.controller.dto.response.AlbumResponse;
import kr.mafoo.photo.domain.AlbumType;
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
    public Flux<AlbumResponse> getAlbums(
            String memberId
    ) {
        return albumService
                .findAllByOwnerMemberId(memberId)
                .map(AlbumResponse::fromEntity);
    }

    @Override
    public Mono<AlbumResponse> getAlbum(
            String memberId,
            String albumId
    ) {
        return albumService
                .findByAlbumId(albumId, memberId)
                .map(AlbumResponse::fromEntity);
    }

    @Override
    public Mono<AlbumResponse> createAlbum(
            String memberId,
            AlbumCreateRequest request
    ){
        AlbumType type = AlbumType.valueOf(request.type());
        return albumService
                .createNewAlbum(memberId, request.name(), type)
                .map(AlbumResponse::fromEntity);
    }

    @Override
    public Mono<AlbumResponse> updateAlbum(String memberId, String albumId, AlbumUpdateRequest request) {
        AlbumType albumType = AlbumType.valueOf(request.type());
        return albumService
                .updateAlbumName(albumId, request.name(), memberId)
                .then(albumService.updateAlbumType(albumId, albumType, memberId))
                .map(AlbumResponse::fromEntity);
    }

    @Override
    public Mono<AlbumResponse> updateAlbumDisplayIndex(String memberId, String albumId, AlbumUpdateDisplayIndexRequest request) {
        return albumService
                .moveAlbumDisplayIndex(albumId, memberId, request.newDisplayIndex())
                .map(AlbumResponse::fromEntity);
    }


    @Override
    public Mono<Void> deleteAlbum(
            String memberId,
            String albumId
    ){
        return albumService
                .deleteAlbumById(albumId, memberId);
    }

}

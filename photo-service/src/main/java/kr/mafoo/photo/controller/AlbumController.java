package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.AlbumApi;
import kr.mafoo.photo.controller.dto.request.AlbumCreateRequest;
import kr.mafoo.photo.controller.dto.request.AlbumRenameRequest;
import kr.mafoo.photo.controller.dto.request.AlbumRetypeRequest;
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
    public Mono<AlbumResponse> updateAlbumName(String memberId, String albumId, AlbumRenameRequest request) {
        return albumService
                .updateAlbumName(albumId, request.name(), memberId)
                .map(AlbumResponse::fromEntity);
    }

    @Override
    public Mono<AlbumResponse> updateAlbumType(String memberId, String albumId, AlbumRetypeRequest request) {
        return albumService
                .updateAlbumType(albumId, request.type(), memberId)
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

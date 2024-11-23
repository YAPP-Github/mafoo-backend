package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.SharedAlbumApi;
import kr.mafoo.photo.controller.dto.response.SharedAlbumResponse;
import kr.mafoo.photo.service.SharedAlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class SharedAlbumController implements SharedAlbumApi {

    private final SharedAlbumService sharedAlbumService;

    @Override
    public Mono<SharedAlbumResponse> getSharedAlbumOwnerAndMemberList(
        String memberId,
        String albumId,
        ServerHttpRequest serverHttpRequest
    ) {
        String authorizationToken = serverHttpRequest.getHeaders().getFirst("Authorization");

        return sharedAlbumService.findSharedAlbumOwnerAndMemberList(albumId, memberId, authorizationToken)
            .flatMap(SharedAlbumResponse::fromDto);
    }

}

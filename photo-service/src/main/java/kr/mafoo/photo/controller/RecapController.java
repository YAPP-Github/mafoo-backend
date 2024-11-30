package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.RecapApi;
import kr.mafoo.photo.controller.dto.request.RecapCreateRequest;
import kr.mafoo.photo.controller.dto.request.RecapCreateRequestOld;
import kr.mafoo.photo.controller.dto.response.RecapResponse;
import kr.mafoo.photo.service.RecapService;
import kr.mafoo.photo.service.RecapServiceOld;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class RecapController implements RecapApi {

    private final RecapServiceOld recapServiceOld;
    private final RecapService recapService;

    @Override
    public Mono<RecapResponse> createRecapOriginal(
            String memberId,
            RecapCreateRequestOld request,
            String sort,
            ServerHttpRequest serverHttpRequest
    ) {
        String authorizationToken = serverHttpRequest.getHeaders().getFirst("Authorization");

        return recapServiceOld.createRecap(request.albumId(), memberId, sort, authorizationToken)
                .map(RecapResponse::fromString);
    }

    @Override
    public Mono<RecapResponse> createRecapVideo(
        String memberId,
        RecapCreateRequest request
    ) {
        return recapService.generateRecapVideo(request.fileUrls(), request.albumId(), memberId)
            .map(RecapResponse::fromDto);
    }

}

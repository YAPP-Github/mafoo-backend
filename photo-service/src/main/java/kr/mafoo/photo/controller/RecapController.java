package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.RecapApi;
import kr.mafoo.photo.controller.dto.request.RecapCreateRequest;
import kr.mafoo.photo.controller.dto.response.RecapResponse;
import kr.mafoo.photo.service.RecapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class RecapController implements RecapApi {

    private final RecapService recapService;

    @Override
    public Mono<RecapResponse> createRecap(
            String memberId,
            RecapCreateRequest request,
            String sort,
            String authorizationToken
    ) {
        return recapService.createRecap(request.albumId(), memberId, sort, authorizationToken)
                .map(RecapResponse::fromString);
    }

}

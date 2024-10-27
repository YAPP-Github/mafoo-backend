package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.RecapApi;
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
            String albumId,
            String sort
    ) {
        return recapService.createRecap(albumId, memberId, sort)
                .map(RecapResponse::fromString);
    }

}

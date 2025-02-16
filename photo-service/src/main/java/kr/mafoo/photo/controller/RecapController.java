package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.RecapApi;
import kr.mafoo.photo.controller.dto.request.RecapCreateRequest;
import kr.mafoo.photo.controller.dto.response.RecapResponse;
import kr.mafoo.photo.service.RecapLambdaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class RecapController implements RecapApi {

    private final RecapLambdaService recapLambdaService;

    @Override
    public Mono<RecapResponse> createRecapVideo(
        String memberId,
        RecapCreateRequest request
    ) {
        return recapLambdaService.generateMafooRecapVideo(request.fileUrls(), request.albumId(), memberId)
            .map(RecapResponse::fromDto);
    }

}

package kr.mafoo.photo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kr.mafoo.photo.exception.MafooRecapLambdaApiFailedException;
import kr.mafoo.photo.service.dto.RecapUrlDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class RecapLambdaService {
    private final WebClient client;

    public RecapLambdaService(@Qualifier("recapLambdaClient") WebClient client) {
        this.client = client;
    }

    public Mono<RecapUrlDto> generateVideo(List<String> recapPhotoUrls) {
        return client
            .post()
            .bodyValue(createRequestBody(recapPhotoUrls))
            .retrieve()
            .onStatus(status -> !status.is2xxSuccessful(), (res) -> Mono.error(new MafooRecapLambdaApiFailedException()))
            .bodyToMono(RecapUrlDto.class);
    }

    private Map<String, Object> createRequestBody(List<String> recapPhotoUrls) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fileUrls", recapPhotoUrls);
        return requestBody;
    }
}


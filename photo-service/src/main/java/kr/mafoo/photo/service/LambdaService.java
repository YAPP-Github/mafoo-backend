package kr.mafoo.photo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kr.mafoo.photo.exception.MafooUserApiFailed;
import kr.mafoo.photo.service.dto.RecapUrlDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class LambdaService {

    @Value("${lambda.endpoint}")
    private String lambdaEndpoint;

    private final WebClient client;

    public Mono<RecapUrlDto> generateRecap(List<String> recapPhotoUrls) {
        return client
            .post()
            .uri(lambdaEndpoint)
            .bodyValue(createRequestBody(recapPhotoUrls))
            .retrieve()
            .onStatus(status -> !status.is2xxSuccessful(), (res) -> Mono.error(new MafooUserApiFailed()))
            .bodyToMono(RecapUrlDto.class);
    }

    private Map<String, Object> createRequestBody(List<String> recapPhotoUrls) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fileUrls", recapPhotoUrls);
        return requestBody;
    }

}


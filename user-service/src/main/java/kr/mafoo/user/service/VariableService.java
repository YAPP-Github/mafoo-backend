package kr.mafoo.user.service;

import java.util.Map;
import kr.mafoo.user.enums.VariableDomain;
import kr.mafoo.user.enums.VariableSort;
import kr.mafoo.user.enums.VariableType;
import kr.mafoo.user.exception.MafooPhotoApiFailedException;
import kr.mafoo.user.util.VariableUriGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class VariableService {

    @Value("${app.gateway.endpoint}")
    String endpoint;

    private final WebClient client;

    public Mono<Map<String, String>> getVariableMap(
        String receiverMemberId,
        VariableDomain domain,
        VariableSort sort,
        VariableType type
    ) {
        String uri = VariableUriGenerator.generate(endpoint, receiverMemberId, domain, sort, type);

        return client
            .get()
            .uri(uri)
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, response -> Mono.empty())
            .onStatus(status -> !status.is2xxSuccessful(), res -> Mono.error(new MafooPhotoApiFailedException()))
            .bodyToMono(new ParameterizedTypeReference<>() {});
    }
}

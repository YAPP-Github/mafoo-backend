package kr.mafoo.user.service;
import kr.mafoo.user.exception.MafooPhotoApiFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class MemberDataService {

    @Value("${app.gateway.endpoint}")
    private String endpoint;

    private final WebClient client;

    public Mono<Void> deleteMemberData(String authorizationToken) {
        return client
            .delete()
            .uri(endpoint + "/photo/v1/member-data")
            .header("Authorization", "Bearer " + authorizationToken)
            .retrieve()
            .onStatus(status -> status.isSameCodeAs(HttpStatus.BAD_REQUEST), (res) -> Mono.empty())
            .onStatus(status -> !status.is2xxSuccessful(), (res) -> Mono.error(new MafooPhotoApiFailedException()))
            .toBodilessEntity()
            .then();
    }

}


package kr.mafoo.photo.service;

import kr.mafoo.photo.exception.MafooUserApiFailed;
import kr.mafoo.photo.service.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ClientResponse;

@RequiredArgsConstructor
@Service
public class MemberService {

    @Value("${app.gateway.endpoint}")
    private String endpoint;

    private final WebClient client;

    public Mono<MemberDto> getMemberInfo(String authorizationToken) {
        return client
                .get()
                .uri(endpoint + "/user/v1/me")
                .header("Authorization", "Bearer " + authorizationToken)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), (res) -> Mono.error(new MafooUserApiFailed()))
                .bodyToMono(MemberDto.class);
    }
}


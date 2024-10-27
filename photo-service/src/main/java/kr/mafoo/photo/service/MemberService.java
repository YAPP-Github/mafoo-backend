package kr.mafoo.photo.service;

import kr.mafoo.photo.service.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ClientResponse;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final WebClient client;

    public Mono<MemberDto> getMemberInfo(String authorizationToken) {
        return client
                .get()
                .uri("https://gateway.mafoo.kr/user/v1/me")
                .header("Authorization", "Bearer " + authorizationToken)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        ClientResponse::createException)
                .bodyToMono(MemberDto.class);
    }
}


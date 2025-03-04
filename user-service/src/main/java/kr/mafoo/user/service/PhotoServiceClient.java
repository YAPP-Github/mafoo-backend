package kr.mafoo.user.service;

import java.util.List;
import java.util.Map;
import kr.mafoo.user.enums.VariableDomain;
import kr.mafoo.user.enums.VariableSort;
import kr.mafoo.user.enums.VariableType;
import kr.mafoo.user.exception.MafooPhotoApiFailedException;
import kr.mafoo.user.service.dto.SharedMemberDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class PhotoServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(PhotoServiceClient.class);

    @Value("${app.gateway.endpoint}")
    private String gatewayEndpoint;

    @Value("${app.photo.endpoint}")
    private String photoEndpoint;

    private final WebClient client;

    public Mono<Void> deleteMemberData(String authorizationToken) {
        return client
            .delete()
            .uri(photoEndpoint + "/v1/member-data")
            .header("Authorization", "Bearer " + authorizationToken)
            .retrieve()
            .onStatus(status -> status.isSameCodeAs(HttpStatus.BAD_REQUEST), (res) -> Mono.empty())
            .onStatus(status -> !status.is2xxSuccessful(), (res) -> Mono.error(new MafooPhotoApiFailedException()))
            .toBodilessEntity()
            .then();
    }

    public Flux<SharedMemberDto> getSharedMemberFluxByAlbumId(String albumId, List<String> memberIdList, String authorizationToken) {
        return client
            .get()
            .uri(gatewayEndpoint + "/v1/shared-members?albumId=" + albumId + "&memberIdList=" + String.join(",", memberIdList))
            .httpRequest(request -> {
                logger.info("Sending request to: {}", request.getURI());
                request.getHeaders().forEach((name, values) -> logger.info("Header: {}={}", name, values));
            })
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, response -> {
                logger.error("BAD_REQUEST error received: {}", response);
                return Mono.empty();
            })
            .onStatus(status -> !status.is2xxSuccessful(), response -> {
                logger.error("Non-2XX response received: {}", response);
                return Mono.error(new MafooPhotoApiFailedException());
            })
            .bodyToFlux(SharedMemberDto.class);
    }

    public Mono<Map<String, String>> getPhotoServiceVariableMap(String receiverMemberId, VariableDomain domain, VariableSort sort, VariableType type) {
        return client.get()
            .uri(generateGetPhotoServiceVariableMapUri(receiverMemberId, domain, sort, type))
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, response -> Mono.empty())
            .onStatus(status -> !status.is2xxSuccessful(), res -> Mono.error(new MafooPhotoApiFailedException()))
            .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
            .defaultIfEmpty(Map.of());
    }

    private String generateGetPhotoServiceVariableMapUri(String receiverMemberId, VariableDomain domain, VariableSort sort, VariableType type) {
        return String.format("%s/v1/%s/variables?memberId=%s&%s%s",
            photoEndpoint,
            domain.getName(),
            receiverMemberId,
            sort.toQueryParam(),
            type != VariableType.NONE ? "&" + type.toQueryParam() : ""
        );
    }

}


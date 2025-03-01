package kr.mafoo.user.service;

import java.util.Map;
import kr.mafoo.user.enums.VariableDomain;
import kr.mafoo.user.enums.VariableSort;
import kr.mafoo.user.enums.VariableType;
import kr.mafoo.user.exception.MafooPhotoApiFailedException;
import kr.mafoo.user.service.dto.SharedMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class PhotoServiceClient {

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

    public Mono<SharedMemberDto> getSharedMemberInfoByAlbumId(String albumId, String memberId, String authorizationToken) {
        return client
            .get()
            .uri(photoEndpoint + "/v1/shared-members?albumId=" + albumId + "&memberId=" + memberId)
            .header("Authorization", "Bearer " + authorizationToken)
            .retrieve()
            .onStatus(status -> status.isSameCodeAs(HttpStatus.BAD_REQUEST), (res) -> Mono.empty())
            .onStatus(status -> !status.is2xxSuccessful(), (res) -> Mono.error(new MafooPhotoApiFailedException()))
            .bodyToMono(SharedMemberDto.class);
    }

}


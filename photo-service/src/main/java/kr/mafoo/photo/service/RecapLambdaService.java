package kr.mafoo.photo.service;

import static kr.mafoo.photo.domain.enums.NotificationType.RECAP_CREATED;
import static kr.mafoo.photo.domain.enums.PermissionLevel.DOWNLOAD_ACCESS;

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
    private final AlbumPermissionVerifier albumPermissionVerifier;

    private final MemberServiceClient memberServiceClient;

    public RecapLambdaService(
        @Qualifier("recapLambdaClient")
        WebClient client,
        AlbumPermissionVerifier albumPermissionVerifier,
        MemberServiceClient memberServiceClient
    ) {
        this.client = client;
        this.albumPermissionVerifier = albumPermissionVerifier;
        this.memberServiceClient = memberServiceClient;
    }

    public Mono<RecapUrlDto> generateMafooRecapVideo(List<String> recapPhotoUrls, String albumId, String requestMemberId) {
        return albumPermissionVerifier.verifyOwnershipOrAccessPermission(albumId, requestMemberId, DOWNLOAD_ACCESS)
                .flatMap(album -> generateVideo(recapPhotoUrls)
                    .flatMap(recapUrlDto -> memberServiceClient
                        .sendScenarioNotification(
                            RECAP_CREATED,
                            List.of(requestMemberId),
                            Map.of(
                                "albumName", album.getName(),
                                "recapUrl", recapUrlDto.recapUrl())
                        )
                        .then(Mono.just(recapUrlDto))
                    )
                );
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


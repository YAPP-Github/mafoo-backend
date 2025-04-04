package kr.mafoo.photo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kr.mafoo.photo.domain.enums.NotificationType;
import kr.mafoo.photo.service.dto.MemberDto;
import kr.mafoo.photo.exception.MafooUserApiFailedException;
import kr.mafoo.photo.service.dto.NotificationDto;
import kr.mafoo.photo.service.dto.NotificationSendRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
public class MemberServiceClient {

    @Value("${app.user.endpoint}")
    private String userEndpoint;
  
    private final WebClient client;


    public Flux<NotificationDto> sendScenarioNotification(NotificationType notificationType, List<String> receiverMemberIds, Map<String, String> variables) {
        return client
            .post()
            .uri(userEndpoint + "/v1/notifications")
            .bodyValue(new NotificationSendRequestDto(notificationType, receiverMemberIds, variables))
            .retrieve()
            .onStatus(status -> !status.is2xxSuccessful(), response ->
                response.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(new MafooUserApiFailedException(response.statusCode().value(), errorBody)))
            )
            .bodyToFlux(NotificationDto.class);
    }

    private Map<String, Object> createSendNotificationRequestBody(NotificationType notificationType, List<String> receiverMemberIds, Map<String, String> variables) {
        Map<String, Object> requestBody = new HashMap<>();

        requestBody.put("notificationType", notificationType);
        requestBody.put("receiverMemberIds", receiverMemberIds);
        requestBody.put("variables", variables);

        return requestBody;
    }

    public Mono<MemberDto> getMemberInfoByToken(String authorizationToken) {
        return client
            .get()
            .uri(userEndpoint + "/v1/me")
            .header("Authorization", "Bearer " + authorizationToken)
            .retrieve()
            .onStatus(status -> !status.is2xxSuccessful(), response ->
                response.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(new MafooUserApiFailedException(response.statusCode().value(), errorBody)))
            )
            .bodyToMono(MemberDto.class);
    }

    public Mono<MemberDto> getMemberInfoById(String memberId) {
        return client
            .get()
            .uri(userEndpoint + "/v1/members/" + memberId)
            .retrieve()
            .onStatus(status -> !status.is2xxSuccessful(), response ->
                response.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(new MafooUserApiFailedException(response.statusCode().value(), errorBody)))
            )
            .bodyToMono(MemberDto.class);
    }

}


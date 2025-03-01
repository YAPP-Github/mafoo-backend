package kr.mafoo.user.service;

import static kr.mafoo.user.enums.VariableDomain.NONE;

import java.util.HashMap;
import java.util.Map;
import kr.mafoo.user.controller.dto.response.MemberResponse;
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

    private final MemberQuery memberQuery;

    public Mono<Map<String, String>> getVariableMap(
        String receiverMemberId,
        VariableDomain domain,
        VariableSort sort,
        VariableType type
    ) {
        return memberQuery.findById(receiverMemberId)
            .map(MemberResponse::fromEntity)
            .flatMap(member -> {
                Map<String, String> variableMap = new HashMap<>();

                variableMap.put("memberId", member.memberId());
                variableMap.put("memberName", member.name());
                variableMap.put("profileImageUrl", member.profileImageUrl());
                variableMap.put("serialNumber", member.serialNumber());

                if (domain.equals(NONE)) {
                    return Mono.just(variableMap);
                }

                String uri = VariableUriGenerator.generate(endpoint, receiverMemberId, domain, sort, type);

                return client.get()
                    .uri(uri)
                    .retrieve()
                    .onStatus(HttpStatus.BAD_REQUEST::equals, response -> Mono.empty())
                    .onStatus(status -> !status.is2xxSuccessful(), res -> Mono.error(new MafooPhotoApiFailedException()))
                    .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                    .defaultIfEmpty(Map.of())
                    .map(responseMap -> {
                        variableMap.putAll(responseMap);
                        return variableMap;
                    });
            });
    }
}

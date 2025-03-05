package kr.mafoo.user.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import kr.mafoo.user.enums.VariableDomain;
import kr.mafoo.user.enums.VariableSort;
import kr.mafoo.user.enums.VariableParam;
import kr.mafoo.user.service.dto.ReceiverMemberDto;
import kr.mafoo.user.util.MapMerger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class VariableService {

    private final PhotoServiceClient photoServiceClient;

    private final MemberQuery memberQuery;

    public Mono<Map<String, String>> getVariableMapWithVariables(
        String receiverMemberId,
        Map<String, String> givenVariables
    ) {
        return generateBaseVariableMap(receiverMemberId)
            .flatMap(receiverMemberVariableMap -> Mono.just(MapMerger.merge(receiverMemberVariableMap, givenVariables)));
    }

    public Mono<Map<String, String>> getVariableMapWithDynamicVariables(
        String receiverMemberId,
        VariableDomain domain,
        VariableSort sort,
        VariableParam type
    ) {
        return generateBaseVariableMap(receiverMemberId)
            .flatMap(receiverMemberVariableMap -> photoServiceClient.getPhotoServiceVariableMap(receiverMemberId, domain, sort, type)
                .flatMap(photoServiceVariableMap -> Mono.just(MapMerger.merge(receiverMemberVariableMap, photoServiceVariableMap)))
            );
    }

    private Mono<Map<String, String>> generateBaseVariableMap(String receiverMemberId) {
        ObjectMapper objectMapper = new ObjectMapper();

        return memberQuery.findById(receiverMemberId)
            .map(ReceiverMemberDto::fromEntity)
            .map(dto -> objectMapper.convertValue(dto, new TypeReference<Map<String, String>>() {}));
    }
}

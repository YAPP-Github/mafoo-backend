package kr.mafoo.user.service;

import java.util.Map;
import kr.mafoo.user.enums.VariableDomain;
import kr.mafoo.user.enums.VariableSort;
import kr.mafoo.user.enums.VariableType;
import kr.mafoo.user.util.MapMerger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class VariableService {

    private final PhotoServiceClient photoServiceClient;

    private final MemberQuery memberQuery;

    public Mono<Map<String, String>> getVariableMapWithoutVariables(String receiverMemberId) {
        return memberQuery.findById(receiverMemberId)
            .flatMap(member -> Mono.just(Map.of(
                "receiverMemberId", member.getId(),
                "receiverName", member.getName(),
                "receiverProfileImageUrl", member.getProfileImageUrl(),
                "receiverSerialNumber", member.getSerialNumber().toString()
            )));
    }

    public Mono<Map<String, String>> getVariableMapWithVariables(
        String receiverMemberId,
        Map<String, String> givenVariables
    ) {
        return getVariableMapWithoutVariables(receiverMemberId)
            .flatMap(receiverMemberVariableMap -> Mono.just(MapMerger.merge(receiverMemberVariableMap, givenVariables)));
    }

    public Mono<Map<String, String>> getVariableMapWithDynamicVariables(
        String receiverMemberId,
        VariableDomain domain,
        VariableSort sort,
        VariableType type
    ) {
        return getVariableMapWithoutVariables(receiverMemberId)
            .flatMap(receiverMemberVariableMap -> photoServiceClient.getPhotoServiceVariableMap(receiverMemberId, domain, sort, type)
                .flatMap(photoServiceVariableMap -> Mono.just(MapMerger.merge(receiverMemberVariableMap, photoServiceVariableMap)))
            );
    }
}

package kr.mafoo.user.util;

import kr.mafoo.user.enums.VariableDomain;
import kr.mafoo.user.enums.VariableSort;
import kr.mafoo.user.enums.VariableType;

public class VariableUriGenerator {
    public static String generate(String endpoint, String receiverMemberId, VariableDomain domain, VariableSort sort, VariableType type) {
        StringBuilder uri = new StringBuilder(endpoint)
            .append("/photo/v1/")
            .append(domain.getName())
            .append("?memberId=").append(receiverMemberId)
            .append("&").append(sort.toQueryParam());

        if (type != VariableType.NONE) {
            uri.append("&").append(type.toQueryParam());
        }

        return uri.toString();
    }
}


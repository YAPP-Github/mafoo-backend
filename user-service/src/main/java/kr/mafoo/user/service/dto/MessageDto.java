package kr.mafoo.user.service.dto;

import java.util.List;
import java.util.Map;
import kr.mafoo.user.enums.ButtonType;
import kr.mafoo.user.enums.RouteType;
import kr.mafoo.user.util.IdGenerator;
import kr.mafoo.user.util.VariableConverter;

public record MessageDto(
    String notificationId,
    List<String> receiverMemberIds,
    List<String> tokens,
    String title,
    String body,
    String route,
    String paramKey,
    ButtonType buttonType
) {
    public static MessageDto fromTemplateWithoutVariables(
        List<String> receiverMemberIds,
        List<String> tokens,
        String title,
        String body,
        RouteType routeType
    ) {
        return new MessageDto(
            IdGenerator.generate(),
            receiverMemberIds,
            tokens,
            title,
            body,
            routeType.getRoute(),
            null,
            null
        );
    }

    public static MessageDto fromTemplateWithVariables(
        List<String> receiverMemberIds,
        List<String> tokens,
        String title,
        String body,
        RouteType routeType,
        Map<String, String> variables
    ) {
        return new MessageDto(
            IdGenerator.generate(),
            receiverMemberIds,
            tokens,
            VariableConverter.convert(title, variables),
            VariableConverter.convert(body, variables),
            routeType.getRoute(),
            routeType.getParamKeyType() != null ? VariableConverter.convert(routeType.getParamKeyType().getPlaceholder(), variables) : null,
            routeType.getButtonType()
        );
    }

}
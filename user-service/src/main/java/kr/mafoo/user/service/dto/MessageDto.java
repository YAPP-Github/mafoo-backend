package kr.mafoo.user.service.dto;

import java.util.List;
import java.util.Map;
import kr.mafoo.user.enums.ButtonType;
import kr.mafoo.user.enums.RouteType;
import kr.mafoo.user.enums.VariablePlaceholder;
import kr.mafoo.user.util.IdGenerator;

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
            convertVariables(title, variables),
            convertVariables(body, variables),
            routeType.getRoute(),
            routeType.getParamKeyType() != null ? convertVariables(routeType.getParamKeyType().getPlaceholder(), variables) : null,
            routeType.getButtonType()
        );
    }

    private static String convertVariables(String text, Map<String, String> variables) {
        StringBuilder result = new StringBuilder(text);

        for (VariablePlaceholder variablePlaceholder : VariablePlaceholder.values()) {
            String placeholder = variablePlaceholder.getPlaceholder();
            String paramKey = variablePlaceholder.getParamKey();

            String replacement = variables.getOrDefault(paramKey, "");

            int index;
            while ((index = result.indexOf(placeholder)) != -1) {
                result.replace(index, index + placeholder.length(), replacement);
            }
        }

        return result.toString();
    }

}
package kr.mafoo.user.service.dto;

import java.util.List;
import java.util.Map;
import kr.mafoo.user.enums.ButtonType;
import kr.mafoo.user.enums.RouteType;
import kr.mafoo.user.enums.VariablePlaceholder;

public record MessageDto(
    List<String> receiverMemberIds,
    List<String> tokens,
    String title,
    String body,
    String route,
    String key,
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
            receiverMemberIds,
            tokens,
            convertVariables(title, variables),
            convertVariables(body, variables),
            routeType.getRoute(),
            convertVariables(routeType.getKeyType().getPlaceholder(), variables),
            routeType.getButtonType()
        );
    }

    private static String convertVariables(String text, Map<String, String> variables) {
        StringBuilder result = new StringBuilder(text);

        for (VariablePlaceholder variablePlaceholder : VariablePlaceholder.values()) {
            String placeholder = variablePlaceholder.getPlaceholder();
            String key = variablePlaceholder.getKey();

            String replacement = variables.getOrDefault(key, "");

            int index;
            while ((index = result.indexOf(placeholder)) != -1) {
                result.replace(index, index + placeholder.length(), replacement);
            }
        }

        return result.toString();
    }

}
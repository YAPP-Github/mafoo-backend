package kr.mafoo.user.service.dto;

import java.util.List;
import java.util.Map;
import kr.mafoo.user.enums.VariablePlaceholder;

public record MessageDto(
    List<String> receiverMemberIds,
    List<String> tokens,
    String title,
    String body
) {
    public static MessageDto fromTemplateWithoutVariables(
        List<String> receiverMemberIds,
        List<String> tokens,
        String title,
        String body
    ) {
        return new MessageDto(
            receiverMemberIds,
            tokens,
            title,
            body
        );
    }

    public static MessageDto fromTemplateWithVariables(
        List<String> receiverMemberIds,
        List<String> tokens,
        String title,
        String body,
        Map<String, String> variables
    ) {
        return new MessageDto(
            receiverMemberIds,
            tokens,
            convertVariables(title, variables),
            convertVariables(body, variables)
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
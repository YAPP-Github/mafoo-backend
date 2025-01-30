package kr.mafoo.user.util.dto;

import java.util.List;
import java.util.Map;
import kr.mafoo.user.enums.VariablePlaceholder;

public record MessageDto(
    List<String> receiverMemberIds,
    String title,
    String body
) {
    public static MessageDto fromTemplateWithoutVariables(
        List<String> receiverMemberIds,
        String title,
        String body
    ) {
        return new MessageDto(
            receiverMemberIds,
            title,
            body
        );
    }

    public static MessageDto fromTemplateWithVariables(
        List<String> receiverMemberIds,
        String title,
        String body,
        Map<String, String> variables
    ) {
        return new MessageDto(
            receiverMemberIds,
            convertWithVariables(title, variables),
            convertWithVariables(body, variables)
        );
    }

    private static String convertWithVariables(String text, Map<String, String> variables) {

        for (VariablePlaceholder variablePlaceholder : VariablePlaceholder.values()) {
            String value = variables.get(variablePlaceholder.getPlaceholderKey());
            text = text.replace(variablePlaceholder.getPlaceholder(), value);
        }

        return text;
    }
}
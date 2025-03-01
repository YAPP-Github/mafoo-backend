package kr.mafoo.user.util;

import java.util.Map;
import kr.mafoo.user.enums.VariablePlaceholder;

public class VariableConverter {
    public static String convert(String text, Map<String, String> variables) {
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


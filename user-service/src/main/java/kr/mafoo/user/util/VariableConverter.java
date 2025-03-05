package kr.mafoo.user.util;

import java.util.Map;
import kr.mafoo.user.enums.NotificationIcon;
import kr.mafoo.user.enums.variables.IconVariable;
import kr.mafoo.user.enums.variables.ParamKeyVariable;
import kr.mafoo.user.enums.variables.Variable;
import kr.mafoo.user.enums.variables.TextVariable;

public class VariableConverter {
    private static String convert(String text, Map<String, String> variables, Variable[] types) {
        StringBuilder result = new StringBuilder(text);

        for (Variable type : types) {
            String placeholder = type.getPlaceholder();
            String jsonKey = type.getJsonKey();
            String replacement = variables.getOrDefault(jsonKey, "");

            int index;
            while ((index = result.indexOf(placeholder)) != -1) {
                result.replace(index, index + placeholder.length(), replacement);
            }
        }

        return result.toString();
    }

    public static String convertIconVariables(String text, Map<String, String> variables) {
        if (NotificationIcon.CONGRATULATION.name().equals(text) || NotificationIcon.STACK.name().equals(text)) {
            return text;
        } else {
            return convert(text, variables, IconVariable.values());
        }
    }

    public static String convertParamKeyVariables(String text, Map<String, String> variables) {
        return convert(text, variables, ParamKeyVariable.values());
    }

    public static String convertTextVariables(String text, Map<String, String> variables) {
        return convert(text, variables, TextVariable.values());
    }
}

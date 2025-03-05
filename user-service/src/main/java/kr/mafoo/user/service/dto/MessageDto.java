package kr.mafoo.user.service.dto;

import static kr.mafoo.user.util.VariableConverter.convertIconVariables;
import static kr.mafoo.user.util.VariableConverter.convertParamKeyVariables;
import static kr.mafoo.user.util.VariableConverter.convertTextVariables;

import java.util.Map;
import kr.mafoo.user.domain.FcmTokenEntity;
import kr.mafoo.user.domain.TemplateEntity;
import kr.mafoo.user.enums.NotificationButton;
import kr.mafoo.user.enums.NotificationIcon;
import kr.mafoo.user.util.IdGenerator;

public record MessageDto(
    String notificationId,
    String receiverMemberId,
    String tokens,
    NotificationIcon icon,
    String title,
    String body,
    String route,
    String paramKey,
    NotificationButton buttonType
) {
    public static MessageDto fromEntities(
        FcmTokenEntity fcmTokenEntity,
        TemplateEntity templateEntity,
        Map<String, String> variables
    ) {
        return new MessageDto(
            IdGenerator.generate(),
            fcmTokenEntity.getOwnerMemberId(),
            fcmTokenEntity.getToken(),
            NotificationIcon.valueOf(convertIconVariables(
                templateEntity.getIcon(), variables
            )),
            convertTextVariables(
                templateEntity.getTitle(), variables
            ),
            convertTextVariables(
                templateEntity.getBody(), variables
            ),
            templateEntity.getRouteType().getRoute(),
            templateEntity.getRouteType().getParamKeyVariable() != null
                ? convertParamKeyVariables(templateEntity.getRouteType().getParamKeyVariable().getPlaceholder(), variables)
                : null,
            templateEntity.getRouteType().getButtonType()
        );
    }
}
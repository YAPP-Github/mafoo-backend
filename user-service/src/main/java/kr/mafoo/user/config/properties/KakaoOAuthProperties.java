package kr.mafoo.user.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@ConfigurationProperties(prefix = "app.oauth.kakao")
@ConfigurationPropertiesBinding
public record KakaoOAuthProperties(
        String clientId,
        String redirectUri,
        String clientSecret
) {
}

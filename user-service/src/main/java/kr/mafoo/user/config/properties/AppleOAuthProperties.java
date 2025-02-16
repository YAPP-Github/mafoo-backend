package kr.mafoo.user.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@ConfigurationProperties(prefix = "app.oauth.apple")
@ConfigurationPropertiesBinding
public record AppleOAuthProperties(
        String clientId,
        String nativeClientId
) {
}

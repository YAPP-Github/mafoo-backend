package kr.mafoo.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean("externalWebClient")
    public WebClient externalServiceWebClient() {
        return WebClient.builder()
                .codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer
                            .defaultCodecs()
                            .maxInMemorySize(16 * 1024 * 1024); // 16MB
                })
                .build();
    }
}

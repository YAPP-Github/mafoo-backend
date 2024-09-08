package kr.mafoo.admin.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean("userServiceWebClient")
    fun userServiceWebClient(): WebClient {
        return WebClient
            .builder()
            .baseUrl("http://user-serivce")
            .codecs { clientCodecConfigurer: ClientCodecConfigurer ->
                clientCodecConfigurer
                    .defaultCodecs()
                    .maxInMemorySize(64 * 1024 * 1024) // 64MB
            }
            .build()
    }

    @Bean("photoServiceWebClient")
    fun photoServiceWebClient(): WebClient {
        return WebClient
            .builder()
            .baseUrl("http://photo-serivce")
            .codecs { clientCodecConfigurer: ClientCodecConfigurer ->
                clientCodecConfigurer
                    .defaultCodecs()
                    .maxInMemorySize(64 * 1024 * 1024) // 64MB
            }
            .build()
    }
}

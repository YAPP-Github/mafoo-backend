package kr.mafoo.admin.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig(
    @Value("\${app.url.user-service}") private val userServiceUrl: String,
    @Value("\${app.url.photo-service}") private val photoServiceUrl: String,
) {
    @Bean("userServiceWebClient")
    fun userServiceWebClient(): WebClient {
        return WebClient
            .builder()
            .baseUrl(userServiceUrl)
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
            .baseUrl(photoServiceUrl)
            .codecs { clientCodecConfigurer: ClientCodecConfigurer ->
                clientCodecConfigurer
                    .defaultCodecs()
                    .maxInMemorySize(64 * 1024 * 1024) // 64MB
            }
            .build()
    }
}

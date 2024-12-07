package kr.mafoo.photo.config;

import io.netty.channel.ChannelOption;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;

@EnableWebFlux
@Configuration
@EnableCaching
public class WebFluxConfig implements WebFluxConfigurer {
    @Override
    public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
        configurer.addCustomResolver(new MemberIdParameterResolver());
    }

    @Primary
    @Bean("externalWebClient")
    public WebClient externalServiceWebClient() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        return WebClient.builder()
                .uriBuilderFactory(factory)
                .codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer
                            .defaultCodecs()
                            .maxInMemorySize(16 * 1024 * 1024); // 16MB
                })
                .build();
    }

    @Bean("recapLambdaClient")
    public WebClient recapLambdaClient(
            @Value("${lambda.endpoint}") String lambdaEndpoint
    ) {
        ConnectionProvider provider = ConnectionProvider.builder("lambda-provider")
                .maxConnections(100)
                .maxIdleTime(Duration.ofSeconds(58))
                .maxLifeTime(Duration.ofSeconds(58))
                .pendingAcquireTimeout(Duration.ofMillis(5000))
                .pendingAcquireMaxCount(-1)
                .evictInBackground(Duration.ofSeconds(30))
                .lifo()
                .metrics(true)
                .build();

        HttpClient httpClient = HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // 연결 타임아웃
                .responseTimeout(Duration.ofSeconds(15))
                .keepAlive(true);

        return WebClient.builder()
                .baseUrl(lambdaEndpoint)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer
                            .defaultCodecs()
                            .maxInMemorySize(16 * 1024 * 1024); // 16MB
                })
                .build();
    }

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setAllowNullValues(false);
        cacheManager.setCacheNames(List.of("albumCount"));
        return cacheManager;
    }
}

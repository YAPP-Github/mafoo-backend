package kr.mafoo.photo.util;

import kr.mafoo.photo.exception.RedirectUriNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

public class WebClientUtil {
    public static Mono<String> getRedirectUri(WebClient client, String url) {
        return client
                .get()
                .uri(url)
                .retrieve()
                .toBodilessEntity()
                .flatMap(response -> {
                    URI redirectUri = response.getHeaders().getLocation();
                    if (redirectUri == null) {
                        return Mono.error(new RedirectUriNotFoundException());
                    } else {
                        return Mono.just(redirectUri.toString());
                    }
                });
    }

    public static Mono<byte[]> getBlob(WebClient client, String url) {
        return client
                .get()
                .uri(url)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToMono(byte[].class);
    }
}

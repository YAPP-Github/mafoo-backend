package kr.mafoo.photo.service.vendors;

import java.net.URI;
import java.net.URISyntaxException;
import kr.mafoo.photo.exception.PhotoQrUrlExpiredException;
import kr.mafoo.photo.exception.RedirectUriNotFoundException;
import kr.mafoo.photo.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class DontLookUpQrVendor implements QrVendor {
    private final WebClient webClient;

    @Override
    public Mono<byte[]> extractImageFromQrUrl(String qrUrl) {
        try {
            URI uri = new URI(qrUrl);
            String path = uri.getPath();
            String[] pathSegments = path.split("/");

            String baseUrl = uri.getScheme() + "://" + uri.getHost() + "/uploads/";
            String imageName = pathSegments[pathSegments.length - 1];
            String imageUrl = baseUrl + imageName;

            return WebClientUtil.getRedirectUri(webClient, qrUrl)
                .flatMap(redirectUri -> {
                    if (redirectUri.endsWith("/delete")) {
                        return Mono.error(new PhotoQrUrlExpiredException());
                    } else {
                        return WebClientUtil.getBlob(webClient, imageUrl);
                    }
                })
                .onErrorResume(
                    RedirectUriNotFoundException.class, e -> WebClientUtil.getBlob(webClient, imageUrl)
                );
        } catch (URISyntaxException e) {
            return Mono.error(new IllegalArgumentException("Invalid QR URL: " + qrUrl, e));
        }
    }

}

package kr.mafoo.photo.service.vendors;

import kr.mafoo.photo.exception.PhotoQrUrlExpiredException;
import kr.mafoo.photo.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class PhotoSignatureQrVendor implements QrVendor {
    private final WebClient webClient;

    @Override
    public Mono<byte[]> extractImageFromQrUrl(String qrUrl) {
        String imageUrl = qrUrl.replace("index.html", "a.jpg");
        return WebClientUtil
                .getBlob(webClient, imageUrl)
                .onErrorMap(e -> new PhotoQrUrlExpiredException());
    }
}

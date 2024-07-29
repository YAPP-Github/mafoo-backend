package kr.mafoo.photo.service.vendors;

import kr.mafoo.photo.exception.PhotoQrUrlExpiredException;
import kr.mafoo.photo.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class MafooQrVendor implements QrVendor {
    private final WebClient webClient;
    private final static String sampleImageUrl
            = "https://kr.object.ncloudstorage.com/mafoo//c8dbdc0d-65d6-490b-ac68-c37d99d494bf";

    @Override
    public Mono<byte[]> extractImageFromQrUrl(String qrUrl) {
        return WebClientUtil
                .getBlob(webClient, qrUrl) //just image url
                .onErrorMap(e -> new PhotoQrUrlExpiredException());
    }
}

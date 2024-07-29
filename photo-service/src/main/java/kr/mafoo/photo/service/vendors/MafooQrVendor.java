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
            = "https://i.ibb.co/VY7s8m1/c8dbdc0d-65d6-490b-ac68-c37d99d494bf.jpg";

    @Override
    public Mono<byte[]> extractImageFromQrUrl(String qrUrl) {
        return WebClientUtil
                .getBlobByAnyMediaType(webClient, sampleImageUrl) //just image url
                .onErrorMap(e -> new PhotoQrUrlExpiredException());
    }
}

package kr.mafoo.photo.service.vendors;

import kr.mafoo.photo.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class MyFourCutQrVendor implements QrVendor {
    private final WebClient webClient;

    @Override
    public Mono<byte[]> extractImageFromQrUrl(String qrUrl) {
        return WebClientUtil.getBlob(webClient, qrUrl); //just image url
    }
}

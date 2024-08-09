package kr.mafoo.photo.service.vendors;

import kr.mafoo.photo.exception.PhotoQrUrlExpiredException;
import kr.mafoo.photo.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class PicDotQrVendor implements QrVendor {
    private final WebClient webClient;

    @Override
    public Mono<byte[]> extractImageFromQrUrl(String qrUrl) {
        String qrCode = extractQrCodeFromUrl(qrUrl);
        String imageUrl = String.format("https://picdot.kr/api/download.php?qrcode=%s&type=P", qrCode);
        return WebClientUtil
                .getBlob(webClient, imageUrl)
                .onErrorMap(e -> new PhotoQrUrlExpiredException());
    }

    private String extractQrCodeFromUrl(String url) {
        return UriComponentsBuilder
                .fromUriString(url)
                .build()
                .getQueryParams()
                .getFirst("qrcode");
    }
}

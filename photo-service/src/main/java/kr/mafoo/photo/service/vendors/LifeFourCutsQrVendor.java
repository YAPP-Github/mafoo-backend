package kr.mafoo.photo.service.vendors;

import kr.mafoo.photo.exception.PhotoQrUrlExpiredException;
import kr.mafoo.photo.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class LifeFourCutsQrVendor implements QrVendor {
    private final WebClient webClient;

    @Override
    public Mono<byte[]> extractImageFromQrUrl(String qrUrl) {
        return WebClientUtil.getRedirectUri(webClient, qrUrl)
                .flatMap(redirectUri -> {
                    String imageUrl = redirectUri.split("path=")[1].replace("index.html", "image.jpg");

                    // TODO : 추후 비디오 URL 추가 예정
                    // String videoUrl = redirectUri.toString().replace("index.html", "video.mp4");

                    return WebClientUtil.getBlob(webClient, imageUrl);
                })
                .onErrorMap(e -> new PhotoQrUrlExpiredException());
    }
}

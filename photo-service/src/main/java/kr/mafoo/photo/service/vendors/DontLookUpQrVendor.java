package kr.mafoo.photo.service.vendors;

import kr.mafoo.photo.exception.PhotoQrUrlExpiredException;
import kr.mafoo.photo.exception.RedirectUriNotFoundException;
import kr.mafoo.photo.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class DontLookUpQrVendor implements QrVendor {
    private final WebClient webClient;

    @Override
    public Mono<byte[]> extractImageFromQrUrl(String qrUrl) {
        String imageName = qrUrl.split(".kr/h/")[1];

        String baseUrl = "https://x.dontlxxkup.kr/uploads/";
        String imageUrl = baseUrl + imageName;

        // TODO : 추후 비디오 URL 추가 예정
        // String videoName = imageName.replace("image", "video").replace(".jpg", ".mp4");
        // String videoUrl = baseUrl + videoName;

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
    }
}

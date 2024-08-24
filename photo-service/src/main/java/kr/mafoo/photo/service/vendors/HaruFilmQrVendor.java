package kr.mafoo.photo.service.vendors;

import kr.mafoo.photo.exception.PhotoQrUrlExpiredException;
import kr.mafoo.photo.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class HaruFilmQrVendor implements QrVendor {
    private final WebClient webClient;

    @Override
    public Mono<byte[]> extractImageFromQrUrl(String qrUrl) {
        String[] urlValueList = qrUrl.split("/@");
        String albumCode = urlValueList[1];

        String baseUrl = urlValueList[0] + "/base_api?command=albumdn&albumCode=";
        String imageUrl = baseUrl + albumCode + "&type=photo&file_name=output.jpg&max=10&limit=+24%20hours";

        // TODO : 추후 비디오 URL 추가 예정
        // String videoUrl = baseUrl + albumCode + "&type=video&max=10&limit=+24 hours";

        return WebClientUtil.getBlob(webClient, imageUrl)
                .onErrorMap(e -> new PhotoQrUrlExpiredException());
    }
}

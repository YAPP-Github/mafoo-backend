package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.BrandType;
import kr.mafoo.photo.exception.PhotoBrandNotExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class QrService {

    private final WebClient externalWebClient;

    protected Mono<byte[]> getFileFromQrUrl(String qrUrl) {
        BrandType brandType = Optional.ofNullable(BrandType.matchBrandType(qrUrl))
                .orElseThrow(PhotoBrandNotExistsException::new);

        return switch (brandType) {
            case LIFE_FOUR_CUTS -> getLifeFourCutsFiles(qrUrl);
            case PHOTOISM -> null;
            case HARU_FILM -> getHaruFilmFiles(qrUrl);
            case DONT_LOOK_UP -> null;
        };

    }

    private Mono<byte[]> getLifeFourCutsFiles(String qrUrl) {

        return getRedirectUri(qrUrl)
                .flatMap(redirectUri -> {
                    // TODO : 추후 비디오 URL 추가 예정
                    // String videoUrl = redirectUri.toString().replace("index.html", "video.mp4");

                    String imageUrl = redirectUri.toString().replace("index.html", "image.jpg");
                    return getFileAsByte(imageUrl);
                });
    }

    private Mono<byte[]> getHaruFilmFiles(String qrUrl) {

        String albumCode = qrUrl.split("/@")[1];

        String baseUrl = "http://haru6.mx2.co.kr/base_api?command=albumdn&albumCode=";
        String imageUrl = baseUrl + albumCode + "&type=photo&file_name=output.jpg&max=10&limit=+24 hours";

        // TODO : 추후 비디오 URL 추가 예정
        // String videoUrl = baseUrl + albumCode + "&type=video&max=10&limit=+24 hours";

        return getFileAsByte(imageUrl);
    }

    private Mono<URI> getRedirectUri(String url) {
        return externalWebClient
                .get()
                .uri(url)
                .retrieve()
                .toBodilessEntity()
                .flatMap(response -> {
                    URI redirectUri = response.getHeaders().getLocation();
                    if (redirectUri == null) {
                        throw new RuntimeException("No redirection URL found");
                    } else {
                        return Mono.just(redirectUri);
                    }
                });
    }

    private Mono<byte[]> getFileAsByte(String url) {
        return externalWebClient
                .get()
                .uri(url)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToMono(byte[].class);
    }


}

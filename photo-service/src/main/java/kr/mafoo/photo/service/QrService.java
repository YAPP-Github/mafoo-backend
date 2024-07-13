package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.BrandType;
import kr.mafoo.photo.exception.PhotoBrandNotExistsException;
import kr.mafoo.photo.exception.PhotoQrUrlExpiredException;
import kr.mafoo.photo.exception.RedirectUriNotFoundException;
import kr.mafoo.photo.service.dto.FileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class QrService {

    private final WebClient externalWebClient;

    public Mono<FileDto> getFileFromQrUrl(String qrUrl) {
        BrandType brandType = Optional.ofNullable(BrandType.matchBrandType(qrUrl))
                .orElseThrow(PhotoBrandNotExistsException::new);

        return switch (brandType) {
            case LIFE_FOUR_CUTS -> createFileDto(brandType, getLifeFourCutsFiles(qrUrl));
            case PHOTOISM -> createFileDto(brandType, getPhotoismFiles(qrUrl));
            case HARU_FILM -> createFileDto(brandType, getHaruFilmFiles(qrUrl));
            case DONT_LOOK_UP -> createFileDto(brandType, getDontLookUpFiles(qrUrl));
        };
    }

    private Mono<FileDto> createFileDto(BrandType brandType, Mono<byte[]> fileMono) {
        return fileMono.map(file -> new FileDto(brandType, file));
    }

    private Mono<byte[]> getLifeFourCutsFiles(String qrUrl) {

        return getRedirectUri(qrUrl)
                .flatMap(redirectUri -> {
                    String imageUrl = extractValueFromUrl(redirectUri, "path=")[1].replace("index.html", "image.jpg");

                    // TODO : 추후 비디오 URL 추가 예정
                    // String videoUrl = redirectUri.toString().replace("index.html", "video.mp4");

                    return getFileAsByte(imageUrl);
                })
                .onErrorMap(e -> new PhotoQrUrlExpiredException());
    }

    private Mono<byte[]> getPhotoismFiles(String qrUrl) {
        return getRedirectUri(qrUrl)
                .flatMap(redirectUri -> {
                    String uid = extractValueFromUrl(redirectUri, "u=")[1];

                    return externalWebClient
                            .post()
                            .uri("https://cmsapi.seobuk.kr/v1/etc/seq/resource")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(Map.of("uid", uid, "appUserId", null))
                            .retrieve()
                            .bodyToMono(LinkedHashMap.class)
                            .flatMap(responseBody -> {
                                LinkedHashMap<String, Object> content = (LinkedHashMap<String, Object>) responseBody.get("content");
                                LinkedHashMap<String, Object> fileInfo = (LinkedHashMap<String, Object>) content.get("fileInfo");
                                String imageUrl = (String) fileInfo.get("picFile.path");

                                return getFileAsByte(imageUrl);
                            });
                })
                .onErrorMap(e -> new PhotoQrUrlExpiredException());
    }

    private Mono<byte[]> getHaruFilmFiles(String qrUrl) {
        String[] urlValueList = extractValueFromUrl(qrUrl, "/@");
        String albumCode = urlValueList[1];

        String baseUrl = urlValueList[0] + "/base_api?command=albumdn&albumCode=";
        String imageUrl = baseUrl + albumCode + "&type=photo&file_name=output.jpg&max=10&limit=+24 hours";

        // TODO : 추후 비디오 URL 추가 예정
        // String videoUrl = baseUrl + albumCode + "&type=video&max=10&limit=+24 hours";

        return getFileAsByte(imageUrl)
                .onErrorMap(e -> new PhotoQrUrlExpiredException());
    }

    private Mono<byte[]> getDontLookUpFiles(String qrUrl) {
        String imageName = extractValueFromUrl(qrUrl, ".kr/image/")[1];

        String baseUrl = "https://x.dontlxxkup.kr/uploads/";
        String imageUrl = baseUrl + imageName;

        // TODO : 추후 비디오 URL 추가 예정
        // String videoName = imageName.replace("image", "video").replace(".jpg", ".mp4");
        // String videoUrl = baseUrl + videoName;

        return getRedirectUri(qrUrl)
                .flatMap(redirectUri -> {
                    if (redirectUri.endsWith("/delete")) {
                        return Mono.error(new PhotoQrUrlExpiredException());
                    } else {
                        return getFileAsByte(imageUrl);
                    }
                })
                .onErrorResume(
                        RedirectUriNotFoundException.class, e -> getFileAsByte(imageUrl)
                );
    }

    private Mono<String> getRedirectUri(String url) {
        return externalWebClient
                .get()
                .uri(url)
                .retrieve()
                .toBodilessEntity()
                .flatMap(response -> {
                    URI redirectUri = response.getHeaders().getLocation();
                    if (redirectUri == null) {
                        return Mono.error(new RedirectUriNotFoundException());
                    } else {
                        return Mono.just(redirectUri.toString());
                    }
                });
    }

    private String[] extractValueFromUrl(String url, String delimiter) {
        return url.split(delimiter);
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

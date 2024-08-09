package kr.mafoo.photo.service.vendors;

import kr.mafoo.photo.exception.PhotoQrUrlExpiredException;
import kr.mafoo.photo.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class PhotoismQrVendor implements QrVendor {
    private final WebClient webClient;

    @Override
    public Mono<byte[]> extractImageFromQrUrl(String qrUrl) {
        return WebClientUtil.getRedirectUri(webClient, qrUrl)
                .flatMap(redirectUri -> {
                    String uid = redirectUri.split("u=")[1];

                    return webClient
                            .post()
                            .uri("https://cmsapi.seobuk.kr/v1/etc/seq/resource")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(Map.of("uid", uid))
                            .retrieve()
                            .bodyToMono(LinkedHashMap.class)
                            .flatMap(responseBody -> {
                                LinkedHashMap<String, Object> content = (LinkedHashMap<String, Object>) responseBody.get("content");
                                LinkedHashMap<String, Object> fileInfo = (LinkedHashMap<String, Object>) content.get("fileInfo");
                                LinkedHashMap<String, Object> picFile = (LinkedHashMap<String, Object>) fileInfo.get("picFile");
                                String imageUrl = (String) picFile.get("path");

                                return WebClientUtil.getBlob(webClient, imageUrl);
                            });
                })
                .onErrorMap(e -> {
                    e.printStackTrace();
                    return new PhotoQrUrlExpiredException();
                });
    }
}

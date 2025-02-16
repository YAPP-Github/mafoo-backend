package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.ObjectStorageApi;
import kr.mafoo.photo.controller.dto.request.*;
import kr.mafoo.photo.controller.dto.response.PreSignedUrlResponse;
import kr.mafoo.photo.service.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class ObjectStorageController implements ObjectStorageApi {

    private final ObjectStorageService objectStorageService;

    @Override
    public Mono<PreSignedUrlResponse> createPreSignedUrls(
            String requestMemberId,
            ObjectStoragePreSignedUrlRequest request
    ) {
        return objectStorageService
                .createPreSignedUrls(request.fileNames(), requestMemberId)
                .map(PreSignedUrlResponse::fromList);
    }

    @Override
    public Mono<PreSignedUrlResponse> createRecapPreSignedUrls(
        String requestMemberId,
        ObjectStorageRecapPreSignedUrlRequest request
    ) {
        return objectStorageService
            .createRecapPreSignedUrls(request.fileCount(), request.albumId(), requestMemberId)
            .map(PreSignedUrlResponse::fromList);
    }
}

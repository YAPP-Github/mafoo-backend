package kr.mafoo.photo.service;

import static kr.mafoo.photo.domain.enums.PermissionLevel.DOWNLOAD_ACCESS;

import java.util.List;
import kr.mafoo.photo.service.dto.RecapUrlDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecapService {

    private final AlbumPermissionVerifier albumPermissionVerifier;
    private final RecapLambdaService recapLambdaService;

    public Mono<RecapUrlDto> generateRecapVideo(List<String> recapPhotoUrls, String albumId, String requestMemberId) {
        return albumPermissionVerifier.verifyOwnershipOrAccessPermission(albumId, requestMemberId, DOWNLOAD_ACCESS)
            .then(recapLambdaService.generateVideo(recapPhotoUrls));
    }

}
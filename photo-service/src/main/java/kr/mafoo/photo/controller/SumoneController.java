package kr.mafoo.photo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.concurrent.atomic.AtomicInteger;
import kr.mafoo.photo.controller.dto.request.ObjectStoragePreSignedUrlRequest;
import kr.mafoo.photo.controller.dto.request.SumoneAlbumCreateRequest;
import kr.mafoo.photo.controller.dto.request.SumoneRecapCreateRequest;
import kr.mafoo.photo.controller.dto.response.*;
import kr.mafoo.photo.controller.dto.request.SumoneBulkUrlRequest;
import kr.mafoo.photo.domain.SumoneEventMappingEntity;
import kr.mafoo.photo.domain.enums.AlbumType;
import kr.mafoo.photo.domain.enums.BrandType;
import kr.mafoo.photo.exception.*;
import kr.mafoo.photo.repository.SumoneEventMappingRepository;
import kr.mafoo.photo.service.AlbumCommand;
import kr.mafoo.photo.service.AlbumQuery;
import kr.mafoo.photo.service.ObjectStorageService;
import kr.mafoo.photo.service.PhotoCommand;
import kr.mafoo.photo.service.PhotoQuery;
import kr.mafoo.photo.service.RecapLambdaService;
import kr.mafoo.photo.service.RecapService;
import kr.mafoo.photo.service.dto.RecapUrlDto;
import kr.mafoo.photo.util.RandomCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Validated
@Tag(name = "썸원 이벤트용 API")
@RestController
@RequestMapping("/v1/sumone")
public class SumoneController {
    private final static String sumoneAlbumCommonName = "2024 연말 결산 이벤트";
    private final static String sumoneAlbumCommonMemberId = "01JDVYCPARAM63X560YSNDXQ4S";

    private final PhotoCommand photoCommand;
    private final AlbumQuery albumQuery;
    private final PhotoQuery photoQuery;
    private final AlbumCommand albumCommand;
    private final ObjectStorageService objectStorageService;
    private final RecapService recapService;
    private final RecapLambdaService recapLambdaService;
    private final SumoneEventMappingRepository sumoneEventMappingRepository;

    @Operation(summary = "통계 api")
    @GetMapping("/summary")
    public Mono<SumoneSummaryResponse> getSummary() {
        return albumQuery
                .countAlbumByAlbumType(AlbumType.SUMONE)
                .map(SumoneSummaryResponse::new);
    }

    @Transactional
    @Operation(summary = "앨범 생성 api")
    @PostMapping("/albums")
    public Mono<SumoneAlbumResponse> createAlbum(
            @RequestBody SumoneAlbumCreateRequest request
            ) {
        if(request.userId() == null || request.userId().isEmpty()) {
            return Mono.error(new DomainException(ErrorCode.REQUEST_INPUT_NOT_VALID));
        }

        return sumoneEventMappingRepository
                .findById(request.userId())
                .switchIfEmpty(sumoneEventMappingRepository.save(SumoneEventMappingEntity.newEventMember(
                        request.userId(),
                        RandomCodeGenerator.generateAlphanumericString(8)
                )))
                .then(albumCommand.addAlbum(sumoneAlbumCommonName, "SUMONE", sumoneAlbumCommonMemberId, "SUMONE_" + request.userId())
                .map(SumoneAlbumResponse::fromEntity));
    }

    @Operation(summary = "앨범에 이미지 url 추가")
    @PostMapping("/albums/{albumId}/photos")
    public Flux<SumonePhotoResponse> addPhotos(
            @PathVariable String albumId,
            @RequestBody SumoneBulkUrlRequest request
    ) {
        return albumQuery.findById(albumId).flatMapMany(album -> {
            if(album.getType() != AlbumType.SUMONE) {
                return Mono.error(new AlbumNotFoundException());
            }
            AtomicInteger displayIndex = new AtomicInteger(album.getPhotoCount());
            return Flux
                    .fromArray(request.fileUrls())
                    .concatMap(fileUrl -> objectStorageService.setObjectPublicRead(fileUrl)
                            .flatMap(fileLink -> photoCommand.addPhoto(fileLink, BrandType.EXTERNAL, albumId, displayIndex.getAndIncrement(), album.getOwnerMemberId()))
                    )
                    .collectList()
                    .flatMapMany(addedPhotos ->
                            albumCommand.increaseAlbumPhotoCount(album, addedPhotos.size())
                                    .thenMany(Flux.fromIterable(addedPhotos))
                    );
        }).map(SumonePhotoResponse::fromEntity);
    }

    @Operation(summary = "앨범에 이미지 조회")
    @GetMapping("/albums/{albumId}/photos")
    public Flux<SumonePhotoResponse> getPhotos(
            @PathVariable String albumId
    ) {
        return albumQuery.findById(albumId).flatMapMany(album -> {
            if(album.getType() != AlbumType.SUMONE) {
                return Mono.error(new AlbumNotFoundException());
            }
            //TODO: add timeout
            return photoQuery
                    .findAllByAlbumIdOrderByCreatedAtDesc(albumId)
                    .onErrorResume(PhotoNotFoundException.class, ex -> Flux.empty());
        }).map(SumonePhotoResponse::fromEntity);
    }

    @Operation(summary = "Pre-signed Url 요청", description = "Pre-signed Url 목록을 발급합니다.")
    @PostMapping("/albums/{albumId}/presigned-urls")
    Mono<PreSignedUrlResponse> createPreSignedUrls(
            @PathVariable String albumId,
            @RequestBody
            ObjectStoragePreSignedUrlRequest request
    ) {
        return albumQuery.findById(albumId).flatMap(album -> {
            if (album.getType() != AlbumType.SUMONE) {
                return Mono.error(new AlbumNotFoundException());
            }
            return objectStorageService
                    .createPreSignedUrls(request.fileNames(), album.getOwnerMemberId())
                    .map(PreSignedUrlResponse::fromStringArray);
        });
    }

    @Operation(summary = "리캡 영상 생성", description = "리캡 영상을 생성합니다.")
    @PostMapping("/albums/{albumId}/recap")
    Mono<RecapUrlDto> createRecapVideo(
        @PathVariable String albumId,
        @RequestBody
        SumoneRecapCreateRequest request
    ) {
        if(request.fileUrls().size() < 2 || request.fileUrls().size() > 10) {
            return Mono.error(new RecapPhotoCountNotValidException());
        }
        return albumQuery.findById(albumId).flatMap(album -> {
            if(album.getType() != AlbumType.SUMONE) {
                return Mono.error(new AlbumNotFoundException());
            }
            //TODO: add timeout
            return recapLambdaService.generateVideo(request.fileUrls());
        });
    }

    @Operation(summary = "초대 코드 반환", description = "초대 코드를 반환합니다")
    @GetMapping("/invite-code")
    Mono<SumoneInviteCodeResponse> getInviteCode(
            @RequestParam String userId
    ) {
        return sumoneEventMappingRepository
                .findById(userId)
                .map(SumoneInviteCodeResponse::fromEntity);
    }
}

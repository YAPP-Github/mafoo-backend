package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.BrandType;
import kr.mafoo.photo.exception.PhotoBrandNotExistsException;
import kr.mafoo.photo.service.dto.FileDto;
import kr.mafoo.photo.service.vendors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class QrService {

    private final LifeFourCutsQrVendor lifeFourCutsQrVendor;
    private final PhotoismQrVendor photoismQrVendor;
    private final DontLookUpQrVendor dontLookUpQrVendor;
    private final HaruFilmQrVendor haruFilmQrVendor;
    private final MyFourCutQrVendor myFourCutQrVendor;
    private final PhotoGrayQrVendor photoGrayQrVendor;
    private final MonoMansionQrVendor monoMansionQrVendor;
    private final PhotoSignatureQrVendor photoSignatureQrVendor;
    private final PicDotQrVendor picDotQrVendor;
    private final MafooQrVendor mafooQrVendor;


    public Mono<FileDto> getFileFromQrUrl(String qrUrl) {
        BrandType brandType = Optional.ofNullable(BrandType.matchBrandType(qrUrl))
                .orElseThrow(PhotoBrandNotExistsException::new);

        QrVendor qrVendor = switch (brandType) {
            case LIFE_FOUR_CUTS -> lifeFourCutsQrVendor;
            case PHOTOISM -> photoismQrVendor;
            case HARU_FILM -> haruFilmQrVendor;
            case DONT_LOOK_UP -> dontLookUpQrVendor;
            case MY_FOUR_CUT -> myFourCutQrVendor;
            case PHOTOGRAY -> photoGrayQrVendor;
            case MONOMANSION -> monoMansionQrVendor;
            case PHOTO_SIGNATURE -> photoSignatureQrVendor;
            case PICDOT -> picDotQrVendor;
            case MAFOO -> mafooQrVendor;
        };

        return createFileDto(brandType, qrVendor.extractImageFromQrUrl(qrUrl));
    }

    private Mono<FileDto> createFileDto(BrandType brandType, Mono<byte[]> fileMono) {
        return fileMono.map(file -> new FileDto(brandType, file));
    }
}

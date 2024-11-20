package kr.mafoo.photo.service;

import kr.mafoo.photo.controller.dto.response.PageResponse;
import kr.mafoo.photo.domain.enums.BrandType;
import kr.mafoo.photo.domain.PhotoEntity;
import kr.mafoo.photo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class PhotoAdminService {
    private final PhotoRepository photoRepository;

    public Mono<PageResponse<PhotoEntity>> queryPhotos(int page, int size, String type, String ownerMemberId) {
        Flux<PhotoEntity> publisher;
         if(type != null) {
            BrandType brandType;
            try {
                brandType = BrandType.valueOf(type);
                publisher = photoRepository.findAllByBrandOrderByPhotoIdDesc(brandType, PageRequest.of(page, size));
            } catch (IllegalArgumentException e) {
                publisher = photoRepository.findAllByOrderByPhotoIdDesc(PageRequest.of(page, size));
            }
        } else if(ownerMemberId != null) {
            publisher = photoRepository.findAllByOwnerMemberIdOrderByPhotoIdDesc(ownerMemberId, PageRequest.of(page, size));
        } else {
            publisher = photoRepository.findAllByOrderByPhotoIdDesc(PageRequest.of(page, size));
        }
        return publisher
                .collectList()
                .zipWith(photoRepository.count())
                .map(tuple -> new PageResponse<>(tuple.getT1(), page, size, tuple.getT2().intValue()));
    }
}

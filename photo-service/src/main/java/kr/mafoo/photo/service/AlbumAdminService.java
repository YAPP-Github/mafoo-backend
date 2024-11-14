package kr.mafoo.photo.service;

import kr.mafoo.photo.controller.dto.response.PageResponse;
import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.AlbumType;
import kr.mafoo.photo.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AlbumAdminService {
    private final AlbumRepository albumRepository;

    public Mono<PageResponse<AlbumEntity>> queryAlbums(int page, int size, String name, String type, String ownerMemberId) {
        Flux<AlbumEntity> publisher;
        if(name != null) {
            publisher = albumRepository.findAllByNameOrderByAlbumIdDesc(name, PageRequest.of(page, size));
        } else if(type != null) {
            AlbumType albumType;
            try {
                albumType = AlbumType.valueOf(type);
                publisher = albumRepository.findAllByTypeOrderByAlbumIdDesc(albumType, PageRequest.of(page, size));
            } catch (IllegalArgumentException e) {
                publisher = albumRepository.findAllByOrderByAlbumIdDesc(PageRequest.of(page, size));
            }
        } else if(ownerMemberId != null) {
            publisher = albumRepository.findAllByOwnerMemberIdOrderByAlbumIdDesc(ownerMemberId, PageRequest.of(page, size));
        } else {
            publisher = albumRepository.findAllByOrderByAlbumIdDesc(PageRequest.of(page, size));
        }
        return publisher
                .collectList()
                .zipWith(albumRepository.count())
                .map(tuple -> new PageResponse<>(tuple.getT1(), page, size, tuple.getT2().intValue()));
    }
}

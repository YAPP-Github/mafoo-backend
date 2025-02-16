package kr.mafoo.photo.service;

import java.util.concurrent.atomic.AtomicInteger;
import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.enums.BrandType;
import kr.mafoo.photo.exception.AlbumNotFoundException;
import kr.mafoo.photo.repository.AlbumRepository;
import kr.mafoo.photo.repository.SumoneEventMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class EventService {

    private final AlbumRepository albumRepository;

    private final PhotoQuery photoQuery;
    private final PhotoCommand photoCommand;

    private final AlbumQuery albumQuery;
    private final AlbumCommand albumCommand;

    private final SumoneEventMappingRepository sumoneEventMappingRepository;

    @Transactional
    public Mono<AlbumEntity> addSumoneAlbum(String albumName, String albumType, String requestMemberId, String inviteCode) {
        AtomicInteger displayIndex = new AtomicInteger(0);
        return albumCommand
            .addAlbum(albumName, albumType, requestMemberId, null)
            .flatMap(album -> sumoneEventMappingRepository
                .findByInviteCode(inviteCode)
                .switchIfEmpty(Mono.error(new AlbumNotFoundException()))
                .flatMap(sumoneEventMappingEntity ->
                    sumoneEventMappingRepository.delete(sumoneEventMappingEntity).then(Mono.just(sumoneEventMappingEntity)))
                .map(entity -> "SUMONE_" + entity.getId())
                .flatMapMany(albumRepository::findAllByExternalId)
                .flatMap(sumoneAlbum ->
                    photoQuery.findAllByAlbumIdOrderByCreatedAtAsc(sumoneAlbum.getAlbumId())
                        .flatMap(photo ->
                            photoCommand.addPhoto(
                                photo.getPhotoUrl(),
                                BrandType.EXTERNAL,
                                album.getAlbumId(),
                                displayIndex.getAndIncrement(),
                                requestMemberId
                            )
                        )
                )
                .then(albumQuery.findById(album.getAlbumId()))
                .flatMap(newAlbum -> albumCommand.increaseAlbumPhotoCount(newAlbum, displayIndex.get())));
    }


}

package kr.mafoo.photo.service;

import static kr.mafoo.photo.domain.enums.PermissionLevel.FULL_ACCESS;
import static kr.mafoo.photo.domain.enums.PermissionLevel.VIEW_ACCESS;

import kr.mafoo.photo.domain.enums.BrandType;
import kr.mafoo.photo.domain.PhotoEntity;
import kr.mafoo.photo.exception.AlbumNotFoundException;
import kr.mafoo.photo.exception.PhotoDisplayIndexIsSameException;
import kr.mafoo.photo.exception.PhotoDisplayIndexNotValidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.LimitedDataBufferList;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class PhotoService {

    private final PhotoQuery photoQuery;
    private final PhotoCommand photoCommand;

    private final PhotoPermissionVerifier photoPermissionVerifier;

    private final AlbumQuery albumQuery;
    private final AlbumCommand albumCommand;

    private final AlbumPermissionVerifier albumPermissionVerifier;

    private final QrService qrService;
    private final ObjectStorageService objectStorageService;

    @Transactional(readOnly = true)
    public Flux<PhotoEntity> findPhotoListByAlbumId(String albumId, String requestMemberId, String sort) {
        String sortMethod = (sort == null) ? "CUSTOM" : sort.toUpperCase();

        return albumPermissionVerifier.verifyOwnershipOrAccessPermission(albumId, requestMemberId, VIEW_ACCESS)
            .onErrorResume(AlbumNotFoundException.class, ex -> Mono.empty())
            .thenMany(
                switch (sortMethod) {
                    case "ASC" -> photoQuery.findAllByAlbumIdOrderByCreatedAtAsc(albumId);
                    case "DESC" -> photoQuery.findAllByAlbumIdOrderByCreatedAtDesc(albumId);
                    case "CUSTOM" -> photoQuery.findAllByAlbumIdOrderByDisplayIndexDesc(albumId);
                    default -> photoQuery.findAllByAlbumIdOrderByDisplayIndexDesc(albumId);
                }
            );
    }

    @Transactional(readOnly = true)
    public Flux<PhotoEntity> findPhotoListByAlbumIdWithoutVerify(String albumId, String sort) {
        String sortMethod = (sort == null) ? "CUSTOM" : sort.toUpperCase();
        return switch (sortMethod) {
            case "ASC" -> photoQuery.findAllByAlbumIdOrderByCreatedAtAsc(albumId);
            case "DESC" -> photoQuery.findAllByAlbumIdOrderByCreatedAtDesc(albumId);
            default -> photoQuery.findAllByAlbumIdOrderByDisplayIndexDesc(albumId);
        };
    }

    @Transactional
    public Mono<PhotoEntity> addPhotoWithQrUrl(String qrUrl) {
        return qrService
            .getFileFromQrUrl(qrUrl)
            .flatMap(fileDto -> objectStorageService.uploadFile(fileDto.fileByte())
                    .flatMap(photoUrl -> photoCommand.addPhotoWithoutOwnerAndAlbum(photoUrl, fileDto.type()))
            );
    }

    @Transactional
    public Flux<PhotoEntity> addPhotoBulkWithFileUrls(String[] fileUrls, String albumId, String requestMemberId) {
        return albumPermissionVerifier.verifyOwnershipOrAccessPermission(albumId, requestMemberId, FULL_ACCESS)
            .flatMapMany(album -> {
                AtomicInteger displayIndex = new AtomicInteger(album.getPhotoCount());

                return Flux.fromArray(fileUrls)
                    .concatMap(fileUrl -> objectStorageService.setObjectPublicRead(fileUrl)
                        .flatMap(fileLink -> photoCommand.addPhoto(fileLink, BrandType.EXTERNAL, albumId, displayIndex.getAndIncrement(), album.getOwnerMemberId()))
                    )
                    .collectList()
                    .flatMapMany(addedPhotos ->
                        albumCommand.increaseAlbumPhotoCount(album, addedPhotos.size())
                            .thenMany(Flux.fromIterable(addedPhotos))
                    );
            });
    }

    @Transactional
    public Flux<PhotoEntity> uploadPhoto(Flux<FilePart> files, String requestMemberId) {
        return files
                .parallel()
                .flatMap(filePart ->
                        filePart.content()
                                .collect(() -> new LimitedDataBufferList(-31), LimitedDataBufferList::add)
                                .filter(list -> !list.isEmpty())
                                .map(list -> list.get(0).factory().join(list))
                                .doOnDiscard(DataBuffer.class, DataBufferUtils::release)
                                .map(dataBuffer -> {
                                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                    dataBuffer.read(bytes);
                                    DataBufferUtils.release(dataBuffer);
                                    return bytes;
                                })
                                .flatMap(bytes -> objectStorageService.uploadFile(bytes)
                                        .flatMap(photoUrl -> photoCommand.addPhoto(photoUrl, BrandType.EXTERNAL, null, 0, requestMemberId)))
                                .subscribeOn(Schedulers.boundedElastic())

                ).sequential();
    }

    @Transactional
    public Mono<PhotoEntity> initPhotoAlbumId(String photoId, String albumId, String requestMemberId) {
        return albumPermissionVerifier.verifyOwnershipOrAccessPermission(albumId, requestMemberId, FULL_ACCESS)
            .flatMap(album -> albumCommand.increaseAlbumPhotoCount(album, 1))
            .flatMap(album -> photoQuery.findByPhotoId(photoId)
                .flatMap(photo -> photoCommand.modifyPhotoAlbumId(photo, albumId, album.getPhotoCount()-1, album.getOwnerMemberId()))
            );
    }

    @Transactional
    public Flux<PhotoEntity> modifyPhotoBulkAlbumId(String[] photoIds, String albumId, String requestMemberId) {
        return albumPermissionVerifier.verifyOwnershipOrAccessPermission(albumId, requestMemberId, FULL_ACCESS)
            .flatMapMany(newAlbum -> {
                AtomicInteger displayIndex = new AtomicInteger(newAlbum.getPhotoCount());

                return Flux.fromArray(photoIds)
                    .concatMap(photoId -> photoQuery.findByPhotoId(photoId)
                        .flatMap(photo -> albumPermissionVerifier.verifyOwnershipOrAccessPermission(photo.getAlbumId(), requestMemberId, FULL_ACCESS)
                            .flatMap(oldAlbum -> albumCommand.decreaseAlbumPhotoCount(oldAlbum, 1))
                            .thenMany(photoCommand.modifyPhotosByAlbumIdToDecreaseDisplayIndexGreaterThan(photo.getAlbumId(), photo.getDisplayIndex()))
                            .then(Mono.just(photo))
                        )
                        .flatMap(photo -> photoCommand.modifyPhotoAlbumId(photo, albumId, displayIndex.getAndIncrement(), newAlbum.getOwnerMemberId())
                        )
                    )
                    .collectList()
                    .flatMapMany(addedPhotos ->
                        albumCommand.increaseAlbumPhotoCount(newAlbum, addedPhotos.size())
                            .thenMany(Flux.fromIterable(addedPhotos))
                    );
            });
    }

    // FIXME : 추후 if문 개선 필요
    @Transactional
    public Mono<PhotoEntity> modifyPhotoDisplayIndex(String photoId, Integer newIndex, String requestMemberId) {
        return photoPermissionVerifier.verifyAccessPermission(photoId, requestMemberId, FULL_ACCESS)
                .flatMap(photoEntity ->
                    albumPermissionVerifier.verifyOwnershipOrAccessPermission(photoEntity.getAlbumId(), requestMemberId, FULL_ACCESS)
                                .flatMap(albumEntity -> {
                                    int targetIndex = albumEntity.getPhotoCount() - newIndex - 1;

                                    if (photoEntity.getDisplayIndex().equals(targetIndex)) {
                                        return Mono.error(new PhotoDisplayIndexIsSameException());
                                    }

                                    if (targetIndex < 0 || targetIndex >= albumEntity.getPhotoCount()) {
                                        return Mono.error(new PhotoDisplayIndexNotValidException());
                                    }

                                    if (photoEntity.getDisplayIndex() < targetIndex) {
                                        return photoCommand
                                                .modifyPhotosByAlbumIdToDecreaseDisplayIndexBetween(photoEntity.getAlbumId(), photoEntity.getDisplayIndex() + 1, targetIndex)
                                                .then(photoCommand.modifyPhotoDisplayIndex(photoEntity, targetIndex));
                                    } else {
                                        return photoCommand
                                                .modifyPhotosByAlbumIdToIncreaseDisplayIndexBetween(photoEntity.getAlbumId(), targetIndex, photoEntity.getDisplayIndex() - 1)
                                                .then(photoCommand.modifyPhotoDisplayIndex(photoEntity, targetIndex));
                                    }
                                })
                );
    }

    @Transactional
    public Mono<Void> removePhoto(String photoId, String requestMemberId) {
        return photoPermissionVerifier.verifyAccessPermission(photoId, requestMemberId, FULL_ACCESS)
            .flatMap(photo -> photoCommand.removePhoto(photo)
                .then(albumQuery.findById(photo.getAlbumId())
                    .flatMap(album -> albumCommand.decreaseAlbumPhotoCount(album, 1))
                ).then()
            );
    }

}

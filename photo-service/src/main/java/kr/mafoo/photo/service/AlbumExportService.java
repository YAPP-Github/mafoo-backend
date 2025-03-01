package kr.mafoo.photo.service;

import kr.mafoo.photo.controller.dto.response.ExportedAlbumResponse;
import kr.mafoo.photo.domain.AlbumExportEntity;
import kr.mafoo.photo.domain.AlbumExportLikeEntity;
import kr.mafoo.photo.domain.AlbumExportNoteEntity;
import kr.mafoo.photo.domain.enums.NoteType;
import kr.mafoo.photo.exception.AlbumExportAlreadyExistsException;
import kr.mafoo.photo.exception.AlbumExportNotFoundException;
import kr.mafoo.photo.repository.AlbumExportLikeRepository;
import kr.mafoo.photo.repository.AlbumExportNoteRepository;
import kr.mafoo.photo.repository.AlbumExportRepository;
import kr.mafoo.photo.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AlbumExportService {

    private final AlbumExportRepository albumExportRepository;
    private final AlbumExportLikeRepository albumExportLikeRepository;
    private final AlbumExportNoteRepository albumExportNoteRepository;

    private final AlbumPermissionVerifier albumPermissionVerifier;
    private final AlbumService albumService;

    public Mono<AlbumExportEntity> createAlbumExport(String albumId, String requesterMemberId) {
        return albumPermissionVerifier
                .verifyOwnership(albumId, requesterMemberId)
                .flatMap(album ->
                        albumExportRepository
                                .existsByAlbumId(albumId)
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new AlbumExportAlreadyExistsException());
                                    }
                                    return Mono.just(album);
                                })
                )
                .flatMap(album -> albumExportRepository.save(AlbumExportEntity.newAlbumExport(album)));
    }

    public Mono<ExportedAlbumResponse> getAlbumByExportId(String exportId, String memberId) {
        return albumExportRepository
                .findById(exportId)
                .switchIfEmpty(Mono.error(new AlbumExportNotFoundException()))
                .flatMap(export -> albumService
                        .findAlbumDetailByIdWithoutVerify(export.getAlbumId())
                        .flatMap(detailDto ->
                                albumExportNoteRepository.countByExportId(exportId)
                                        .flatMap(noteCount -> {
                                                    Mono<Boolean> likeChain = memberId != null ? albumExportLikeRepository.existsByExportIdAndMemberId(exportId, memberId) : Mono.just(false);
                                                    return likeChain
                                                            .flatMap(exists -> albumExportLikeRepository.countByExportId(exportId)
                                                                    .map(likeCount -> ExportedAlbumResponse.fromDto(detailDto, likeCount, noteCount, exists)));
                                                }
                                        )
                        )
                );

    }

    public Mono<Void> likeExport(String memberId, String exportId) {
        return albumExportLikeRepository.existsByExportIdAndMemberId(exportId, memberId)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.empty();
                    }
                    return albumExportLikeRepository.save(AlbumExportLikeEntity.newLike(exportId, memberId)).then();
                });
    }

    public Mono<Void> unlikeExport(String memberId, String exportId) {
        return albumExportLikeRepository.deleteByExportIdAndMemberId(exportId, memberId);
    }

    public Mono<AlbumExportNoteEntity> createNote(String exportId, NoteType type, String memberId, String content, String nickname) {
        return albumExportRepository
                .findById(exportId)
                .flatMap(export -> albumExportNoteRepository.save(AlbumExportNoteEntity.newAlbumExportNote(exportId, type, memberId, content, nickname)));
    }

    public Flux<AlbumExportNoteEntity> getNotesByExportId(String exportId) {
        return albumExportNoteRepository.findAllByExportId(exportId);
    }

    public Mono<AlbumExportEntity> getAlbumExportByExportId(String exportId) {
        return albumExportRepository.findById(exportId).switchIfEmpty(Mono.error(new AlbumExportNotFoundException()));
    }
}

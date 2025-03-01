package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.AlbumExportApi;
import kr.mafoo.photo.controller.dto.request.AlbumExportCreateRequest;
import kr.mafoo.photo.controller.dto.request.AlbumExportNoteCreateRequest;
import kr.mafoo.photo.controller.dto.response.AlbumExportNoteResponse;
import kr.mafoo.photo.controller.dto.response.AlbumExportResponse;
import kr.mafoo.photo.controller.dto.response.ExportedAlbumResponse;
import kr.mafoo.photo.controller.dto.response.PhotoResponse;
import kr.mafoo.photo.service.AlbumExportService;
import kr.mafoo.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class AlbumExportController implements AlbumExportApi {
    private final AlbumExportService albumExportService;
    private final PhotoService photoService;

    @Override
    public Mono<AlbumExportResponse> createExport(String memberId, AlbumExportCreateRequest albumExportCreateRequest) {
        return albumExportService
                .createAlbumExport(albumExportCreateRequest.albumId(), memberId)
                .map(AlbumExportResponse::fromEntity);
    }

    @Override
    public Mono<ExportedAlbumResponse> getExportedAlbum(String memberId, String exportId) {
        return albumExportService.getAlbumByExportId(exportId, memberId);
    }

    @Override
    public Flux<PhotoResponse> getExportedAlbumPhotos(String exportId) {
        return albumExportService
                .getAlbumExportByExportId(exportId)
                .flatMapMany(export -> photoService.findPhotoListByAlbumIdWithoutVerify(export.getAlbumId(), "DESC"))
                .map(PhotoResponse::fromEntity);
    }

    @Override
    public Mono<Void> likeExport(String memberId, String exportId) {
        return albumExportService.likeExport(memberId, exportId);
    }

    @Override
    public Mono<Void> unlikeExport(String memberId, String exportId) {
        return albumExportService.unlikeExport(memberId, exportId);
    }

    @Override
    public Mono<AlbumExportNoteResponse> createNote(String memberId, String exportId, AlbumExportNoteCreateRequest noteCreateRequest) {
        String actualMemberId = memberId == null ? "MAFOONOTE_ANONYMOUS_MEMBER" : memberId;
        return albumExportService
                .createNote(exportId, noteCreateRequest.type(), actualMemberId, noteCreateRequest.content(), noteCreateRequest.nickname())
                .map(AlbumExportNoteResponse::fromEntity);
    }

    @Override
    public Flux<AlbumExportNoteResponse> getNotes(String exportId) {
        return albumExportService
                .getNotesByExportId(exportId)
                .map(AlbumExportNoteResponse::fromEntity);
    }
}

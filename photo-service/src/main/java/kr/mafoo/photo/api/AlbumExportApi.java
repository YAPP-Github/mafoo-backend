package kr.mafoo.photo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.mafoo.photo.annotation.RequestMemberId;
import kr.mafoo.photo.controller.dto.request.AlbumExportCreateRequest;
import kr.mafoo.photo.controller.dto.request.AlbumExportNoteCreateRequest;
import kr.mafoo.photo.controller.dto.response.AlbumExportNoteResponse;
import kr.mafoo.photo.controller.dto.response.AlbumExportResponse;
import kr.mafoo.photo.controller.dto.response.ExportedAlbumResponse;
import kr.mafoo.photo.controller.dto.response.PhotoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "내보내기 관련 API", description = "내보내기 관련 API")
@RequestMapping("/v1/exports")
public interface AlbumExportApi {
    @Operation(summary = "내보내기 생성")
    @PostMapping
    Mono<AlbumExportResponse> createExport(
            @RequestMemberId String memberId,
            @RequestBody AlbumExportCreateRequest albumExportCreateRequest
    );

    @Operation(summary = "내보내기 앨범 내용 조회")
    @GetMapping("/{exportId}/album")
    Mono<ExportedAlbumResponse> getExportedAlbum(
            @RequestMemberId String memberId,
            @PathVariable String exportId
    );

    @Operation(summary = "내보내기 앨범 내 사진 조회")
    @GetMapping("/{exportId}/album/photos")
    Flux<PhotoResponse> getExportedAlbumPhotos(
            @PathVariable String exportId
    );

    @Operation(summary = "내보내기 좋아요")
    @PostMapping("/{exportId}/like")
    Mono<Void> likeExport(
            @RequestMemberId String memberId,
            @PathVariable String exportId
    );

    @Operation(summary = "내보내기 좋아요 취소")
    @PostMapping("/{exportId}/unlike")
    Mono<Void> unlikeExport(
            @RequestMemberId String memberId,
            @PathVariable String exportId
    );

    @Operation(summary = "방명록 생성")
    @PostMapping("/{exportId}/notes")
    Mono<AlbumExportNoteResponse> createNote(
            @RequestMemberId String memberId,
            @PathVariable String exportId,
            @RequestBody AlbumExportNoteCreateRequest noteCreateRequest
    );

    @Operation(summary = "방명록 목록 조회")
    Flux<AlbumExportNoteResponse> getNotes(
            @RequestMemberId String memberId,
            @PathVariable String exportId
    );
}

package kr.mafoo.photo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.mafoo.photo.annotation.RequestMemberId;
import kr.mafoo.photo.annotation.ULID;
import kr.mafoo.photo.controller.dto.response.SharedAlbumResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Validated
@Tag(name = "공유 앨범 관련 API", description = "공유 앨범 상세 조회 API")
@RequestMapping("/v1/shared-albums")
public interface SharedAlbumApi {
    @Operation(summary = "공유 앨범 상세 조회", description = "공유 앨범의 상세 정보를 조회합니다.")
    @GetMapping("/{albumId}")
    Mono<SharedAlbumResponse> getSharedAlbumOwnerAndMemberList(
            @RequestMemberId
            String memberId,

            @ULID
            @Parameter(description = "앨범 ID", example = "test_album_id")
            @PathVariable
            String albumId,

            // Authorization Header를 받아올 목적
            ServerHttpRequest serverHttpRequest
    );

}

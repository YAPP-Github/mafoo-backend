package kr.mafoo.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.mafoo.user.annotation.RequestMemberId;
import kr.mafoo.user.controller.dto.response.MemberDetailResponse;
import kr.mafoo.user.controller.dto.response.MemberResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "사용자 관련 API", description = "사용자 정보 조회 API")
@Validated
@RequestMapping("/v1/members")
public interface MemberApi {
    @Operation(summary = "공유 앨범 대상 사용자 검색", description = "키워드로 사용자를 검색합니다. (이름으로 검색)")
    @GetMapping
    Flux<MemberDetailResponse> getMemberListByNameForSharedAlbum(
        @RequestMemberId
        @Parameter(hidden = true)
        String requesterId,

        @Parameter(description = "검색어", example = "사람")
        @RequestParam
        String keyword,

        @Parameter(description = "앨범 ID", example = "test_album_id")
        @RequestParam
        String albumId,

        ServerHttpRequest serverHttpRequest
    );

    @Operation(summary = "사용자 단건 조회", description = "사용자 단건 정보를 조회합니다.")
    @GetMapping("/{memberId}")
    Mono<MemberResponse> getMember(
        @Parameter(description = "사용자 ID", example = "test_member_id")
        @PathVariable
        String memberId
    );
}

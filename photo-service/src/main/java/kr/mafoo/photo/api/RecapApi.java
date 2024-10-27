package kr.mafoo.photo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.mafoo.photo.annotation.RequestMemberId;
import kr.mafoo.photo.annotation.ULID;
import kr.mafoo.photo.controller.dto.response.RecapResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Validated
@Tag(name = "리캡 관련 API", description = "리캡 생성 API")
@RequestMapping("/v1/recaps")
public interface RecapApi {
    @Operation(summary = "리캡 생성", description = "앨범의 리캡을 생성합니다.")
    @PostMapping
    Mono<RecapResponse> createRecap(
            @RequestMemberId
            String memberId,

            @ULID
            @Parameter(description = "앨범 ID", example = "test_album_id")
            @RequestParam
            String albumId,

            @Parameter(description = "정렬 종류", example = "ASC | DESC")
            @RequestParam(required = false)
            String sort
    );

}

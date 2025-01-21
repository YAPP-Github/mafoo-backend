package kr.mafoo.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.mafoo.user.annotation.RequestMemberId;
import kr.mafoo.user.annotation.ULID;
import kr.mafoo.user.controller.dto.request.TemplateCreateRequest;
import kr.mafoo.user.controller.dto.request.TemplateUpdateRequest;
import kr.mafoo.user.controller.dto.response.TemplateResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@Tag(name = "템플릿 관련 API", description = "템플릿 조회, 생성, 수정, 삭제 등 API")
@RequestMapping("/v1/notifications/templates")
public interface TemplateApi {
    @Operation(summary = "템플릿 목록 조회")
    @GetMapping
    Flux<TemplateResponse> getTemplateList(
        @RequestMemberId
        String memberId
    );

    @Operation(summary = "템플릿 단건 상세 조회")
    @GetMapping("/{templateId}")
    Mono<TemplateResponse> getTemplate(
        @RequestMemberId
        String memberId,

        @ULID
        @Parameter(description = "템플릿 ID", example = "test_template_id")
        @PathVariable
        String templateId
    );

    @Operation(summary = "템플릿 생성")
    @PostMapping
    Mono<TemplateResponse> createTemplate(
        @RequestMemberId
        String memberId,

        @Valid
        @RequestBody
        TemplateCreateRequest request
    );

    @Operation(summary = "템플릿 수정")
    @PutMapping("/{templateId}")
    Mono<TemplateResponse> updateTemplate(
        @RequestMemberId
        String memberId,

        @ULID
        @Parameter(description = "템플릿 ID", example = "test_template_id")
        @PathVariable
        String templateId,

        @Valid
        @RequestBody
        TemplateUpdateRequest request
    );

    @Operation(summary = "템플릿 삭제")
    @DeleteMapping("/{templateId}")
    Mono<Void> deleteTemplate(
        @RequestMemberId
        String memberId,

        @ULID
        @Parameter(description = "템플릿 ID", example = "test_template_id")
        @PathVariable
        String templateId
    );
}

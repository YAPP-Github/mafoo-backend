package kr.mafoo.user.controller;

import kr.mafoo.user.api.TemplateApi;
import kr.mafoo.user.controller.dto.request.TemplateCreateRequest;
import kr.mafoo.user.controller.dto.request.TemplateUpdateRequest;
import kr.mafoo.user.controller.dto.response.TemplateDetailResponse;
import kr.mafoo.user.controller.dto.response.TemplateResponse;
import kr.mafoo.user.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class TemplateController implements TemplateApi {

    private final TemplateService templateService;

    @Override
    public Flux<TemplateResponse> getTemplateList(
        String memberId
    ){
        return templateService
            .findTemplateList()
            .map(TemplateResponse::fromEntity);
    }

    @Override
    public Mono<TemplateDetailResponse> getTemplate(
        String memberId,
        String templateId
    ){
        return templateService
            .findTemplateById(templateId)
            .map(TemplateDetailResponse::fromDto);
    }

    @Override
    public Mono<TemplateResponse> createTemplate(
        String memberId,
        TemplateCreateRequest request
    ){
        return templateService
            .addTemplate(
                request.notificationType(),
                request.thumbnailImageUrl(),
                request.url(),
                request.title(),
                request.body()
            )
            .map(TemplateResponse::fromEntity);
    }

    @Override
    public Mono<TemplateResponse> updateTemplate(
        String memberId,
        String templateId,
        TemplateUpdateRequest request
    ){
        return templateService
            .modifyTemplate(
                templateId,
                request.notificationType(),
                request.thumbnailImageUrl(),
                request.url(),
                request.title(),
                request.body()
            )
            .map(TemplateResponse::fromEntity);
    }

    @Override
    public Mono<Void> deleteTemplate(
        String memberId,
        String templateId
    ){
        return templateService
            .removeTemplate(templateId);
    }
}

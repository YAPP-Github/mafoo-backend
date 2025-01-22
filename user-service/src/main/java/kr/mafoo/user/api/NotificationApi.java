package kr.mafoo.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.mafoo.user.annotation.RequestMemberId;
import kr.mafoo.user.controller.dto.request.NotificationBulkDeleteRequest;
import kr.mafoo.user.controller.dto.request.NotificationBulkUpdateIsReadRequest;
import kr.mafoo.user.controller.dto.response.NotificationResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@Tag(name = "알림 관련 API", description = "알림 조회, 전송, 수정, 삭제 등 API")
@RequestMapping("/v1/notifications")
public interface NotificationApi {

    @Operation(summary = "알림 목록 조회")
    @GetMapping
    Flux<NotificationResponse> getNotificationList(
        @RequestMemberId
        String memberId
    );

    @Operation(summary = "알림 읽음 여부 n건 수정")
    @PutMapping
    Flux<NotificationResponse> updateNotificationBulkIsRead(
        @RequestMemberId
        String memberId,

        @Valid
        @RequestBody
        NotificationBulkUpdateIsReadRequest request
    );

    @Operation(summary = "알림 n건 삭제")
    @DeleteMapping
    Mono<Void> deleteNotificationBulk(
        @RequestMemberId
        String memberId,

        @Valid
        @RequestBody
        NotificationBulkDeleteRequest request
    );
}

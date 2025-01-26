package kr.mafoo.user.controller;

import kr.mafoo.user.api.NotificationApi;
import kr.mafoo.user.controller.dto.request.NotificationBulkDeleteRequest;
import kr.mafoo.user.controller.dto.request.NotificationBulkUpdateIsReadRequest;
import kr.mafoo.user.controller.dto.request.NotificationSendRequest;
import kr.mafoo.user.controller.dto.response.NotificationDetailResponse;
import kr.mafoo.user.controller.dto.response.NotificationResponse;
import kr.mafoo.user.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class NotificationController implements NotificationApi {

    private final NotificationService notificationService;

    @Override
    public Flux<NotificationDetailResponse> getNotificationList(
        String memberId
    ){
        return notificationService
            .findNotificationListByMemberId(memberId)
            .map(NotificationDetailResponse::fromDto);
    }

    @Override
    public Flux<NotificationResponse> sendNotification(
        String memberId,
        NotificationSendRequest request
    ){
        return Flux.empty();
    }

    @Override
    public Flux<NotificationResponse> updateNotificationBulkIsRead(
        String memberId,
        NotificationBulkUpdateIsReadRequest request
    ){
        return notificationService
            .updateNotificationBulkIsRead(request.notificationIds(), memberId)
            .map(NotificationResponse::fromEntity);
    }

    @Override
    public Mono<Void> deleteNotificationBulk(
        String memberId,
        NotificationBulkDeleteRequest request
    ){
        return notificationService
            .removeNotificationBulk(request.notificationIds(), memberId);
    }
}

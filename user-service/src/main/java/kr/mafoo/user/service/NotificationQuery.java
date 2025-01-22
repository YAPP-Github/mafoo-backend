package kr.mafoo.user.service;

import kr.mafoo.user.domain.NotificationEntity;
import kr.mafoo.user.exception.NotificationNotFoundException;
import kr.mafoo.user.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotificationQuery {

    private final NotificationRepository notificationRepository;

    public Mono<NotificationEntity> findById(String notificationId) {
        return notificationRepository.findById(notificationId)
            .switchIfEmpty(Mono.error(new NotificationNotFoundException()));
    }

    public Flux<NotificationEntity> findAllByReceiverMemberId(String receiverMemberId) {
        return notificationRepository.findAllByReceiverMemberId(receiverMemberId)
            .switchIfEmpty(Mono.error(new NotificationNotFoundException()));
    }
}

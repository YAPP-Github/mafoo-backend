package kr.mafoo.user.scheduler;

import static kr.mafoo.user.enums.ReservationStatus.ACTIVE;

import java.time.LocalDateTime;
import java.util.List;
import kr.mafoo.user.domain.NotificationEntity;
import kr.mafoo.user.domain.ReservationEntity;
import kr.mafoo.user.exception.ReservationNotFoundException;
import kr.mafoo.user.service.NotificationService;
import kr.mafoo.user.service.ReservationCommand;
import kr.mafoo.user.service.ReservationQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationQuery reservationQuery;
    private final ReservationCommand reservationCommand;

    private final NotificationService notificationService;

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public Flux<NotificationEntity> checkReservation() {
        return reservationQuery.findAllByStatusAndSendAtBefore(ACTIVE, LocalDateTime.now())
            .onErrorResume(ReservationNotFoundException.class, ex -> Flux.empty())
            .collectList()
            .filter(reservations -> !reservations.isEmpty())
            .flatMapMany(reservations -> reservationCommand.modifyReservationAfterSent(reservations)
                .thenMany(this.processReservation(reservations))
            );
    }

    public Flux<NotificationEntity> processReservation(
        List<ReservationEntity> reservations) {
        return Flux.fromIterable(reservations)
            .flatMap(reservation -> notificationService.sendNotificationByReservation(
                reservation.getTemplateId(),
                reservation.getReceiverMemberIds(),
                reservation.getVariableDomain(),
                reservation.getVariableSort(),
                reservation.getVariableType()
            ));
    }
}

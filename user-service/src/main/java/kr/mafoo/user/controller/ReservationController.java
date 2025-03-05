package kr.mafoo.user.controller;

import kr.mafoo.user.api.ReservationApi;
import kr.mafoo.user.controller.dto.request.ReservationCreateRequest;
import kr.mafoo.user.controller.dto.request.ReservationUpdateRequest;
import kr.mafoo.user.controller.dto.response.ReservationResponse;
import kr.mafoo.user.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class ReservationController implements ReservationApi {

    private final ReservationService reservationService;

    @Override
    public Mono<ReservationResponse> createReservation(
        String memberId,
        ReservationCreateRequest request
    ){
        return reservationService
            .addReservation(
                request.templateId(),
                request.status(),
                request.variableDomain(),
                request.variableParam(),
                request.variableSort(),
                request.receiverMemberIds(),
                request.sendAt(),
                request.sendRepeatInterval()
            )
            .map(ReservationResponse::fromEntity);
    }

    @Override
    public Mono<ReservationResponse> updateReservation(
        String memberId,
        String reservationId,
        ReservationUpdateRequest request
    ){
        return reservationService
            .modifyReservation(
                reservationId,
                request.templateId(),
                request.status(),
                request.variableDomain(),
                request.variableParam(),
                request.variableSort(),
                request.receiverMemberIds(),
                request.sendAt(),
                request.sendRepeatInterval()
            )
            .map(ReservationResponse::fromEntity);
    }

    @Override
    public Mono<Void> deleteReservation(
        String memberId,
        String reservationId
    ){
        return reservationService
            .removeReservation(reservationId);
    }
}

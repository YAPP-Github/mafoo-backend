package kr.mafoo.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.mafoo.user.annotation.RequestMemberId;
import kr.mafoo.user.annotation.ULID;
import kr.mafoo.user.controller.dto.request.ReservationCreateRequest;
import kr.mafoo.user.controller.dto.request.ReservationUpdateRequest;
import kr.mafoo.user.controller.dto.response.ReservationResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Validated
@Tag(name = "예약 관련 API", description = "예약 생성, 수정, 삭제 등 API")
@RequestMapping("/v1/notifications/reservations")
public interface ReservationApi {
    @Operation(summary = "예약 생성")
    @PostMapping
    Mono<ReservationResponse> createReservation(
        @RequestMemberId
        String memberId,

        @Valid
        @RequestBody
        ReservationCreateRequest request
    );

    @Operation(summary = "예약 수정")
    @PutMapping("/{reservationId}")
    Mono<ReservationResponse> updateReservation(
        @RequestMemberId
        String memberId,

        @ULID
        @Parameter(description = "예약 ID", example = "test_reservation_id")
        @PathVariable
        String reservationId,

        @Valid
        @RequestBody
        ReservationUpdateRequest request
    );

    @Operation(summary = "예약 삭제")
    @DeleteMapping("/{reservationId}")
    Mono<Void> deleteReservation(
        @RequestMemberId
        String memberId,

        @ULID
        @Parameter(description = "알림 ID", example = "test_reservation_id")
        @PathVariable
        String reservationId
    );
}

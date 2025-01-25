package kr.mafoo.user.domain;

import java.time.LocalDateTime;
import kr.mafoo.user.enums.ReservationStatus;
import kr.mafoo.user.enums.VariableDomain;
import kr.mafoo.user.enums.VariableSort;
import kr.mafoo.user.enums.VariableType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@NoArgsConstructor
@Table("notification_reservation")
public class ReservationEntity implements Persistable<String> {
    @Id
    @Column("notification_reservation_id")
    private String reservationId;

    @Column("notification_template_id")
    private String templateId;

    @Column("status")
    private ReservationStatus status;

    @Column("variable_domain")
    private VariableDomain variableDomain;

    @Column("variable_type")
    private VariableType variableType;

    @Column("variable_sort")
    private VariableSort variableSort;

    @Column("receiver_member_ids")
    private String receiverMemberIds;

    @Column("send_at")
    private LocalDateTime sendAt;

    @Column("send_repeat_interval")
    private Integer sendRepeatInterval;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Transient
    private boolean isNew = false;

    @Override
    public boolean equals(Object obj) {
       if (this == obj) return true;
       if (obj == null || getClass() != obj.getClass()) return false;

        ReservationEntity that = (ReservationEntity) obj;
        return reservationId.equals(that.reservationId);
    }

    @Override
    public int hashCode() {
        return reservationId.hashCode();
    }

    @Override
    public String getId() {
        return reservationId;
    }

    public ReservationEntity updateStatus(ReservationStatus status) {
        this.status = status;
        return this;
    }

    public ReservationEntity updateSendAt(LocalDateTime sendAt) {
        this.sendAt = sendAt;
        return this;
    }

    public ReservationEntity updateReservation(String templateId, ReservationStatus status, VariableDomain variableDomain, VariableType variableType, VariableSort variableSort, String receiverMemberIds, LocalDateTime sendAt, Integer sendRepeatInterval) {
        this.templateId = templateId;
        this.status = status;
        this.variableDomain = variableDomain;
        this.variableType = variableType;
        this.variableSort = variableSort;
        this.receiverMemberIds = receiverMemberIds;
        this.sendAt = sendAt;
        this.sendRepeatInterval = sendRepeatInterval;
        return this;
    }

    public static ReservationEntity newReservation(String reservationId, String templateId, ReservationStatus status, VariableDomain variableDomain, VariableType variableType, VariableSort variableSort, String receiverMemberIds, LocalDateTime sendAt, Integer sendRepeatInterval) {
        ReservationEntity reservation = new ReservationEntity();
        reservation.reservationId = reservationId;
        reservation.templateId = templateId;
        reservation.status = status;
        reservation.variableDomain = variableDomain;
        reservation.variableType = variableType;
        reservation.variableSort = variableSort;
        reservation.receiverMemberIds = receiverMemberIds;
        reservation.sendAt = sendAt;
        reservation.sendRepeatInterval = sendRepeatInterval;
        reservation.isNew = true;
        return reservation;
    }
}

package kr.mafoo.user.domain;

import java.time.LocalDateTime;
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
@Table("notification")
public class NotificationEntity implements Persistable<String> {
    @Id
    @Column("notification_id")
    private String notificationId;

    @Column("notification_template_id")
    private String templateId;

    @Column("receiver_member_id")
    private String receiverMemberId;

    @Column("title")
    private String title;

    @Column("body")
    private String body;

    @Column("is_read")
    private Boolean isRead;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Column("deleted_at")
    private LocalDateTime deletedAt;

    @Transient
    private boolean isNew = false;

    @Override
    public boolean equals(Object obj) {
       if (this == obj) return true;
       if (obj == null || getClass() != obj.getClass()) return false;

        NotificationEntity that = (NotificationEntity) obj;
        return notificationId.equals(that.notificationId);
    }

    @Override
    public int hashCode() {
        return notificationId.hashCode();
    }

    @Override
    public String getId() {
        return notificationId;
    }

    public NotificationEntity updateNotificationIsReadTrue() {
        this.isRead = true;
        return this;
    }

    public static NotificationEntity newNotification(String notificationId, String templateId, String receiverMemberId, String title, String body) {
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.notificationId = notificationId;
        notificationEntity.templateId = templateId;
        notificationEntity.receiverMemberId = receiverMemberId;
        notificationEntity.title = title;
        notificationEntity.body = body;
        notificationEntity.isNew = true;
        return notificationEntity;
    }
}

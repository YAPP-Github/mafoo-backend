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
@Table("fcm_token")
public class FcmTokenEntity implements Persistable<String> {
    @Id
    @Column("fcm_token_id")
    private String fcmTokenId;

    @Column("owner_member_id")
    private String ownerMemberId;

    @Column("token")
    private String token;

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

        FcmTokenEntity that = (FcmTokenEntity) obj;

        return fcmTokenId.equals(that.fcmTokenId);
    }

    @Override
    public int hashCode() {
        return fcmTokenId.hashCode();
    }

    @Override
    public String getId() {
        return fcmTokenId;
    }

    public FcmTokenEntity updateToken(String token) {
        this.token = token;
        return this;
    }

    public static FcmTokenEntity newFcmToken(String fcmTokenId, String ownerMemberId, String token) {
        FcmTokenEntity fcmToken = new FcmTokenEntity();
        fcmToken.fcmTokenId = fcmTokenId;
        fcmToken.ownerMemberId = ownerMemberId;
        fcmToken.token = token;
        fcmToken.isNew = true;
        return fcmToken;
    }
}

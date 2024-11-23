package kr.mafoo.photo.domain;

import java.time.LocalDateTime;
import java.util.Optional;
import kr.mafoo.photo.domain.enums.PermissionLevel;
import kr.mafoo.photo.domain.enums.ShareStatus;
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
@Table("shared_member")
public class SharedMemberEntity implements Persistable<String> {
    @Id
    @Column("id")
    private String sharedMemberId;

    @Column("share_status")
    private ShareStatus shareStatus;

    @Column("permission_level")
    private PermissionLevel permissionLevel;

    @Column("member_id")
    private String memberId;

    @Column("album_id")
    private String albumId;

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

        SharedMemberEntity that = (SharedMemberEntity) obj;
        return sharedMemberId.equals(that.sharedMemberId);
    }

    @Override
    public int hashCode() {
        return sharedMemberId.hashCode();
    }

    @Override
    public String getId() {
        return sharedMemberId;
    }

    public SharedMemberEntity updateShareStatus(ShareStatus shareStatus) {
        this.shareStatus = shareStatus;
        return this;
    }

    public SharedMemberEntity updatePermissionLevel(PermissionLevel permissionLevel) {
        this.permissionLevel = permissionLevel;
        return this;
    }

    public static SharedMemberEntity newSharedMember(String sharedMemberId, Optional<ShareStatus> shareStatus, PermissionLevel permissionLevel, String memberId, String albumId) {
        SharedMemberEntity sharedMember = new SharedMemberEntity();
        sharedMember.sharedMemberId = sharedMemberId;
        sharedMember.shareStatus = shareStatus.orElse(ShareStatus.PENDING);
        sharedMember.permissionLevel = permissionLevel;
        sharedMember.memberId = memberId;
        sharedMember.albumId = albumId;
        sharedMember.isNew = true;
        return sharedMember;
    }
}

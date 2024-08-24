package kr.mafoo.photo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table("permission")
public class PermissionEntity implements Persistable<String> {
    @Id
    @Column("id")
    private String permissionId;

    @Column("type")
    private PermissionType type;

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

        PermissionEntity that = (PermissionEntity) obj;
        return permissionId.equals(that.permissionId);
    }

    @Override
    public int hashCode() {
        return permissionId.hashCode();
    }

    @Override
    public String getId() {
        return permissionId;
    }

    public static PermissionEntity newPermission(String permissionId, PermissionType type, String memberId, String albumId) {
        PermissionEntity permission = new PermissionEntity();
        permission.permissionId = permissionId;
        permission.type = type;
        permission.memberId = memberId;
        permission.albumId = albumId;
        permission.isNew = true;
        return permission;
    }
}

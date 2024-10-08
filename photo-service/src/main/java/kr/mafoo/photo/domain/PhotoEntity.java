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
@Table("photo")
public class PhotoEntity implements Persistable<String> {
    @Id
    @Column("id")
    private String photoId;

    @Column("url")
    private String photoUrl;

    @Column("brand")
    private BrandType brand;

    @Column("owner_member_id")
    private String ownerMemberId;

    @Column("album_id")
    private String albumId;

    @Column("display_index")
    private Integer displayIndex;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Transient
    private boolean isNew = false;

    @Override
    public boolean equals(Object obj) {
       if (this == obj) return true;
       if (obj == null || getClass() != obj.getClass()) return false;

        PhotoEntity that = (PhotoEntity) obj;
        return photoId.equals(that.photoId);
    }

    @Override
    public int hashCode() {
        return photoId.hashCode();
    }

    @Override
    public String getId() {
        return photoId;
    }

    public Boolean hasOwnerMemberId() {
        return this.getOwnerMemberId() != null;
    }

    public PhotoEntity updateOwnerMemberId(String ownerMemberId) {
        this.ownerMemberId = ownerMemberId;
        return this;
    }

    public PhotoEntity updateAlbumId(String albumId) {
        this.albumId = albumId;
        return this;
    }

    public PhotoEntity updateDisplayIndex(Integer displayIndex) {
        this.displayIndex = displayIndex;
        return this;
    }

    public static PhotoEntity newPhoto(String photoId, String photoUrl, BrandType brandType, String ownerMemberId) {
        PhotoEntity photo = new PhotoEntity();
        photo.photoId = photoId;
        photo.photoUrl = photoUrl;
        photo.brand = brandType;
        photo.ownerMemberId = ownerMemberId;
        photo.displayIndex = 0;
        photo.isNew = true;
        photo.createdAt = LocalDateTime.now();
        photo.updatedAt = LocalDateTime.now();
        return photo;
    }
}

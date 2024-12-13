package kr.mafoo.photo.domain;

import kr.mafoo.photo.domain.enums.AlbumType;
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
@Table("album")
public class AlbumEntity implements Persistable<String> {
    @Id
    @Column("id")
    private String albumId;

    @Column("name")
    private String name;

    @Column("type")
    private AlbumType type;

    @Column("photo_count")
    private Integer photoCount;

    @Column("owner_member_id")
    private String ownerMemberId;

    @Column("display_index")
    private Integer displayIndex;

    @Column("external_id")
    private String externalId;

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

        AlbumEntity that = (AlbumEntity) obj;
        return albumId.equals(that.albumId);
    }

    @Override
    public int hashCode() {
        return albumId.hashCode();
    }

    @Override
    public String getId() {
        return albumId;
    }

    public AlbumEntity updateOwnerMemberId(String newOwnerMemberId) {
        this.ownerMemberId = newOwnerMemberId;
        return this;
    }

    public AlbumEntity updateDisplayIndex(int newDisplayIndex) {
        this.displayIndex = newDisplayIndex;
        return this;
    }

    public AlbumEntity updateName(String newName) {
        this.name = newName;
        return this;
    }

    public AlbumEntity updateType(AlbumType newType) {
        this.type = newType;
        return this;
    }

    public AlbumEntity increasePhotoCount(int count) {
        this.photoCount += count;
        return this;
    }

    public AlbumEntity decreasePhotoCount(int count) {
        this.photoCount -= count;
        return this;
    }

    public AlbumEntity setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public static AlbumEntity newAlbum(String albumId, String albumName, AlbumType albumType, String ownerMemberId, String externalId) {
        AlbumEntity album = new AlbumEntity();
        album.albumId = albumId;
        album.name = albumName;
        album.type = albumType;
        album.externalId = externalId;
        album.ownerMemberId = ownerMemberId;
        album.isNew = true;
        album.photoCount = 0;
        album.displayIndex = 0;
        return album;
    }
}

package kr.mafoo.photo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
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

    @Column("photoCount")
    private Integer photoCount;

    @Column("owner_member_id")
    private String ownerMemberId;

    @Column("display_index")
    private Long displayIndex;

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

    public AlbumEntity updateName(String newName) {
        this.name = newName;
        return this;
    }

    public AlbumEntity updateType(AlbumType newType) {
        this.type = newType;
        return this;
    }

    public AlbumEntity increasePhotoCount() {
        this.photoCount += 1;
        return this;
    }

    public AlbumEntity decreasePhotoCount() {
        this.photoCount -= 1;
        return this;
    }

    public static AlbumEntity newAlbum(String albumId, String albumName, AlbumType albumType, String ownerMemberId) {
        AlbumEntity album = new AlbumEntity();
        album.albumId = albumId;
        album.name = albumName;
        album.type = albumType;
        album.ownerMemberId = ownerMemberId;
        album.isNew = true;
        album.photoCount = 0;
        return album;
    }
}

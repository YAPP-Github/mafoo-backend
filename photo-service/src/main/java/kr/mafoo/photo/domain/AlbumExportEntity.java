package kr.mafoo.photo.domain;

import java.time.LocalDateTime;
import kr.mafoo.photo.util.IdGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@NoArgsConstructor
@Table("album_export")
public class AlbumExportEntity implements Persistable<String> {
    @Id
    @Column("id")
    private String exportId;

    @Column("album_id")
    private String albumId;

    @Column("view_count")
    private Long viewCount;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @Override
    public String getId() {
        return exportId;
    }

    @Transient
    private boolean isNew = false;

    public static AlbumExportEntity newAlbumExport(AlbumEntity albumEntity) {
        AlbumExportEntity albumExportEntity = new AlbumExportEntity();
        albumExportEntity.exportId = IdGenerator.generate();
        albumExportEntity.albumId = albumEntity.getAlbumId();
        albumExportEntity.viewCount = 0L;
        albumExportEntity.isNew = true;
        return albumExportEntity;
    }

}

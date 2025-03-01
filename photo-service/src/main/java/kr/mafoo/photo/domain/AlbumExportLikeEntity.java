package kr.mafoo.photo.domain;

import java.time.LocalDateTime;
import kr.mafoo.photo.domain.key.AlbumExportLikeEntityKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@NoArgsConstructor
@Table("album_export_like")
public class AlbumExportLikeEntity implements Persistable<AlbumExportLikeEntityKey> {
    @Column("export_id")
    private String exportId;

    @Column("member_id")
    private String memberId;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @Transient
    private boolean isNew = false;

    @Override
    public AlbumExportLikeEntityKey getId() {
        return new AlbumExportLikeEntityKey(exportId, memberId);
    }

    public static AlbumExportLikeEntity newLike(String exportId, String memberId) {
        AlbumExportLikeEntity albumExportEntity = new AlbumExportLikeEntity();
        albumExportEntity.memberId = memberId;
        albumExportEntity.exportId = exportId;
        albumExportEntity.isNew = true;
        return albumExportEntity;
    }

}

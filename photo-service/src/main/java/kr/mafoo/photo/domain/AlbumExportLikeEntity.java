package kr.mafoo.photo.domain;

import java.time.LocalDateTime;
import kr.mafoo.photo.domain.key.AlbumExportLikeEntityKey;
import kr.mafoo.photo.util.IdGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@NoArgsConstructor
@Table("album_export_like")
public class AlbumExportLikeEntity implements Persistable<AlbumExportLikeEntityKey> {
    @Id
    @Column("export_id")
    private String exportId;

    @Column("member_id")
    private String memberId;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;


    @Override
    public AlbumExportLikeEntityKey getId() {
        return new AlbumExportLikeEntityKey(exportId, memberId);
    }

    @Override
    public boolean isNew() {
        return true;
    }

    public static AlbumExportLikeEntity newLike(String exportId, String memberId) {
        AlbumExportLikeEntity albumExportEntity = new AlbumExportLikeEntity();
        albumExportEntity.memberId = memberId;
        albumExportEntity.exportId = exportId;
        return albumExportEntity;
    }

}

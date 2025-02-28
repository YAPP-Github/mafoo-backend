package kr.mafoo.photo.domain;

import kr.mafoo.photo.domain.enums.NoteType;
import kr.mafoo.photo.util.IdGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@NoArgsConstructor
@Table("album_export_note")
public class AlbumExportNoteEntity implements Persistable<String> {
    @Id
    @Column("id")
    private String noteId;

    @Column("export_id")
    private String exportId;

    @Column("type")
    private NoteType type;

    @Column("member_id")
    private String memberId;

    @Column("content")
    private String content;

    @Column("nickname")
    private String nickname;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @Override
    public String getId() {
        return noteId;
    }

    @Override
    public boolean isNew() {
        return true;
    }

    public static AlbumExportNoteEntity newAlbumExportNote(String exportId, NoteType type, String memberId, String content, String nickname) {
        AlbumExportNoteEntity albumExportNoteEntity = new AlbumExportNoteEntity();
        albumExportNoteEntity.noteId = IdGenerator.generate();
        albumExportNoteEntity.type = type;
        albumExportNoteEntity.exportId = exportId;
        albumExportNoteEntity.memberId = memberId;
        albumExportNoteEntity.content = content;
        albumExportNoteEntity.nickname = nickname;
        return albumExportNoteEntity;
    }
}

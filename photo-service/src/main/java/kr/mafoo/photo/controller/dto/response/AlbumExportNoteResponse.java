package kr.mafoo.photo.controller.dto.response;

import kr.mafoo.photo.domain.AlbumExportNoteEntity;
import kr.mafoo.photo.domain.enums.NoteType;

public record AlbumExportNoteResponse(
        String noteId,
        String exportId,
        NoteType type,
        String nickname,
        String content
) {
    public static AlbumExportNoteResponse fromEntity(AlbumExportNoteEntity entity) {
        return new AlbumExportNoteResponse(entity.getNoteId(), entity.getExportId(), entity.getType(), entity.getNickname(), entity.getContent());
    }
}

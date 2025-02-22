
package kr.mafoo.photo.controller.dto.request;

import kr.mafoo.photo.domain.enums.NoteType;

public record AlbumExportNoteCreateRequest(
        NoteType type,
        String nickname,
        String content
) {

}

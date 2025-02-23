package kr.mafoo.photo.domain.key;

import java.io.Serializable;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class AlbumExportLikeEntityKey implements Serializable {
    private final String exportId;
    private final String memberId;
}

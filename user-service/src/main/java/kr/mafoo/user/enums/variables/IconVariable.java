package kr.mafoo.user.enums.variables;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IconVariable implements Variable {
    CONGRATULATION(null, null),
    STACK(null, null),

    ALBUM_TYPE("albumType", "{{앨범 종류}}"),
    SHARED_MEMBER_ALBUM_TYPE("shareTargetAlbumType", "{{공유 대상 앨범 종류}}"),
    ;

    private final String jsonKey;
    private final String placeholder;
}


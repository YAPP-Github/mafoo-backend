package kr.mafoo.user.enums;

import static kr.mafoo.user.enums.ButtonType.INVITATION_ACCEPT;
import static kr.mafoo.user.enums.VariablePlaceholder.ALBUM_ID;
import static kr.mafoo.user.enums.VariablePlaceholder.RECAP_URL;
import static kr.mafoo.user.enums.VariablePlaceholder.SHARED_MEMBER_ALBUM_ID;
import static kr.mafoo.user.enums.VariablePlaceholder.SHARED_MEMBER_ID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RouteType {
    ALBUM_CREATE("AlbumCreate", null, null),
    ALBUM("AlbumDetail", ALBUM_ID, null),

    RECAP("Recap", RECAP_URL, null),

    SHARED_MEMBER_CREATE("AddFriend", null, null),
    SHARED_MEMBER_INVITATION("Album", SHARED_MEMBER_ID, INVITATION_ACCEPT),
    SHARED_MEMBER_LIST("SharedFriend", SHARED_MEMBER_ALBUM_ID, null),

    SHARED_MEMBER_ALBUM("AlbumDetail", SHARED_MEMBER_ALBUM_ID, null)
    ;

    private final String route;
    private final VariablePlaceholder keyType;
    private final ButtonType buttonType;
}


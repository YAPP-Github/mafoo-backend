package kr.mafoo.user.enums;

import static kr.mafoo.user.enums.NotificationButton.INVITATION_ACCEPT;
import static kr.mafoo.user.enums.variables.ParamKeyVariable.ALBUM_ID;
import static kr.mafoo.user.enums.variables.ParamKeyVariable.RECAP_URL;
import static kr.mafoo.user.enums.variables.ParamKeyVariable.SHARED_MEMBER_ALBUM_ID;
import static kr.mafoo.user.enums.variables.ParamKeyVariable.SHARED_MEMBER_ID;

import kr.mafoo.user.enums.variables.ParamKeyVariable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationRoute {
    ALBUM_CREATE("AlbumCreate", null, null),
    ALBUM("AlbumDetail", ALBUM_ID, null),

    RECAP("Recap", RECAP_URL, null),

    SHARED_MEMBER_CREATE("AddFriend", null, null),
    SHARED_MEMBER_INVITATION("Album", SHARED_MEMBER_ID, INVITATION_ACCEPT),
    SHARED_MEMBER_LIST("SharedFriend", SHARED_MEMBER_ALBUM_ID, null),

    SHARED_MEMBER_ALBUM("AlbumDetail", SHARED_MEMBER_ALBUM_ID, null)
    ;

    private final String route;
    private final ParamKeyVariable paramKeyVariable;
    private final NotificationButton buttonType;
}


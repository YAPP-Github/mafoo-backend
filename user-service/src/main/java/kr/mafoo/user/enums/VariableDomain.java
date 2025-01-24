package kr.mafoo.user.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum VariableDomain {
    NONE(null, null),
    MEMBER("member", "member_id"),
    ALBUM("album", "album_id"),
    SHARE_MEMBER("share_member", null)
    ;

    private final String tableName;
    private final String fkName;
}


package kr.mafoo.user.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum VariableType {
    NONE(null),

    HEART("albumType"),
    FIRE("albumType"),
    BASKETBALL("albumType"),
    BUILDING("albumType"),
    STARFALL("albumType"),
    SMILE_FACE("albumType"),
    SUMONE("albumType"),

    FULL_ACCESS("permissionLevel"),
    DOWNLOAD_ACCESS("permissionLevel"),
    VIEW_ACCESS("permissionLevel"),

    ACCEPTED("inviteStatus"),
    PENDING("inviteStatus"),
    REJECTED("inviteStatus"),
    ;

    private final String columnName;

    public String toQueryParam() {
        return (this != NONE) ? columnName + "=" + this : "";
    }
}


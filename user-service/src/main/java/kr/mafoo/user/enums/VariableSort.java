package kr.mafoo.user.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum VariableSort {
    PHOTO_COUNT_MIN("photoCount", "asc"),
    PHOTO_COUNT_MAX("photoCount", "desc"),

    NEWEST("createdAt", "asc"),
    OLDEST("createdAt", "desc"),
    ;

    private final String columnName;
    private final String order;

    public String toQueryParam() {
        return "sort=" + columnName + "&order=" + order;
    }
}


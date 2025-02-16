package kr.mafoo.user.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum VariableSort {
    PHOTO_COUNT_MIN("PHOTO_COUNT", "ASC"),
    PHOTO_COUNT_MAX("PHOTO_COUNT", "DESC"),

    NEWEST("CREATED_AT", "ASC"),
    OLDEST("CREATED_AT", "DESC"),
    ;

    private final String columnName;
    private final String order;

    public String toQueryParam() {
        return "sort=" + columnName + "&order=" + order;
    }
}


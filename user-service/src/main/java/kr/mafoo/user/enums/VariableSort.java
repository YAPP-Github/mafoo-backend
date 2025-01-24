package kr.mafoo.user.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum VariableSort {
    COUNT_MIN("photoCount", "COUNT", "DESC"),
    COUNT_MAX("photoCount", "COUNT", "ASC"),
    NEWEST("updatedAt", "createdAt", "DESC"),
    OLDEST("updatedAt", "createdAt", "ASC")
    ;

    private final String albumColumnName;
    private final String shareMemberColumnName;
    private final String order;
}


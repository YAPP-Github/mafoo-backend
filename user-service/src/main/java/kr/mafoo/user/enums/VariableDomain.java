package kr.mafoo.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VariableDomain {
    NONE(null),

    MEMBER("members"),
    ALBUM("albums"),
    SHARE_MEMBER("shared-members"),
    ;

    private final String name;
}


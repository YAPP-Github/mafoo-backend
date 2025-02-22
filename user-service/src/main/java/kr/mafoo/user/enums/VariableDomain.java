package kr.mafoo.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VariableDomain {
    NONE(null),

    ALBUM("albums"),

    SHARE_MEMBER_IN_OWNED_ALBUM("shared-members/owned-albums"),
    SHARE_MEMBER_IN_SHARED_ALBUM("shared-members/shared-albums"),
    ;

    private final String name;
}


package kr.mafoo.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    MEMBER_NOT_FOUND("ME0001", "사용자를 찾을 수 없습니다");
    private final String code;
    private final String message;
}

package kr.mafoo.photo.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ALBUM_NOT_FOUND("AE0001", "앨범을 찾을 수 없습니다"),

    ;
    private final String code;
    private final String message;
}

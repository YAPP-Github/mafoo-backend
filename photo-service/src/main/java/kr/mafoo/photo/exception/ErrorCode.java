package kr.mafoo.photo.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ALBUM_NOT_FOUND("AE0001", "앨범을 찾을 수 없습니다"),
    PHOTO_NOT_FOUND("PE0001", "사진을 찾을 수 없습니다"),
    PHOTO_BRAND_NOT_EXISTS("PE002", "사진 브랜드가 존재하지 않습니다"),
    PHOTO_QR_URL_EXPIRED("PE003", "사진 저장을 위한 QR URL이 만료되었습니다"),

    ;
    private final String code;
    private final String message;
}

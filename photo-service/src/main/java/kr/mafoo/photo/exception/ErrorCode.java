package kr.mafoo.photo.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    REDIRECT_URI_NOT_FOUND("EX0001", "리다이렉트 URI를 찾을 수 없습니다"),
    REQUEST_INPUT_NOT_VALID("EX0002", "입력 값이 올바르지 않습니다."),

    ALBUM_NOT_FOUND("AE0001", "앨범을 찾을 수 없습니다"),
    ALBUM_DISPLAY_INDEX_IS_SAME("AE0002", "옮기려는 대상 앨범 인덱스가 같습니다"),
    ALBUM_OWNER_MISMATCH("AE0003", "앨범의 소유자가 아닙니다"),
    ALBUM_OWNER_CHANGE_DENIED("AE0004", "공유된 사용자에게만 소유자를 넘길 수 있습니다"),

    PHOTO_NOT_FOUND("PE0001", "사진을 찾을 수 없습니다"),
    PHOTO_BRAND_NOT_EXISTS("PE0002", "사진 브랜드가 존재하지 않습니다"),
    PHOTO_QR_URL_EXPIRED("PE0003", "사진 저장을 위한 QR URL이 만료되었습니다"),
    PHOTO_DISPLAY_INDEX_IS_SAME("PE0004", "옮기려는 대상 사진 인덱스가 같습니다"),
    PHOTO_DISPLAY_INDEX_NOT_VALID("PE0005", "옮기려는 대상 사진 인덱스가 유효하지 않습니다"),
    PHOTO_OWNER_ALREADY_ASSIGNED("PE0006", "이미 소유자가 존재하는 사진입니다"),

    SHARED_MEMBER_NOT_FOUND("SE0001", "공유 사용자를 찾을 수 없습니다"),
    SHARED_MEMBER_DUPLICATED("SE0002", "동일한 공유 사용자가 존재합니다"),
    SHARED_MEMBER_STATUS_NOT_ACCEPTED("SE0003", "공유 요청이 수락되지 않았습니다"),
    SHARED_MEMBER_PERMISSION_DENIED("SE0004", "공유 사용자의 권한이 충족되지 않습니다"),

    PRE_SIGNED_URL_EXCEED_MAXIMUM("OE0001", "한 번에 생성할 수 있는 Pre-signed url 최대치를 초과했습니다"),
    PRE_SIGNED_URL_BANNED_FILE_TYPE("OE0002", "Pre-signed url 발급이 허용되지 않는 파일 형식입니다"),

    MAFOO_USER_API_FAILED("MUE0001", "마푸의 user-service API 호출이 실패했습니다"),

    MAFOO_RECAP_LAMBDA_API_FAILED("MRE0001", "마푸의 recap-service AWS Lambda API 호출이 실패했습니다")

    ;
    private final String code;
    private final String message;
}

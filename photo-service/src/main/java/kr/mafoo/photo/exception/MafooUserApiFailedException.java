package kr.mafoo.photo.exception;

import lombok.Getter;

@Getter
public class MafooUserApiFailedException extends RuntimeException {
    private final int statusCode;

    public MafooUserApiFailedException(int statusCode, String responseBody) {
        super("Mafoo user-service API 호출 실패 (HTTP " + statusCode + "): " + responseBody);
        this.statusCode = statusCode;
    }
}

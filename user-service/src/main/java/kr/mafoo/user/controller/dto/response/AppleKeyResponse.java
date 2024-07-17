package kr.mafoo.user.controller.dto.response;

public record AppleKeyResponse(
        String kty,
        String kid,
        String use,
        String alg,
        String n,
        String e
) {
}

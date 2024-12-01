package kr.mafoo.photo.domain.enums;

import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BrandType {
    LIFE_FOUR_CUTS(Pattern.compile("https://api\\.life4cut\\.net/.*")),
    PHOTOISM(Pattern.compile("https://qr\\.seobuk\\.kr/.*")),
    HARU_FILM(Pattern.compile("http://haru\\d+\\.mx\\d+\\.co\\.kr/.*")),
    DONT_LOOK_UP(Pattern.compile("https://\\w+\\.dontlxxkup\\.kr/.*")),
    MY_FOUR_CUT(Pattern.compile("https://firebasestorage\\.googleapis\\.com:443/v0/b/my4ccu\\.appspot\\.com/.*")),
    PHOTOGRAY(Pattern.compile("https://pgshort\\.aprd\\.io/.*")),
    MONOMANSION(Pattern.compile("https://monomansion\\.net/.*")),
    PHOTO_SIGNATURE(Pattern.compile("http://photoqr3\\.kr/.*")),
    PICDOT(Pattern.compile("https://picdot\\.kr/.*")),
    MAFOO(Pattern.compile("https://mafoo\\.kr/.*")),
    EXTERNAL(Pattern.compile("https://mafoo")),
    ;

    private final Pattern urlPattern;

    public boolean matches(String qrUrl) {
        return urlPattern.matcher(qrUrl).matches();
    }

    public static BrandType matchBrandType(String qrUrl) {
        for (BrandType brandType : BrandType.values()) {
            if (brandType.matches(qrUrl)) {
                return brandType;
            }
        }
        return null;
    }
}


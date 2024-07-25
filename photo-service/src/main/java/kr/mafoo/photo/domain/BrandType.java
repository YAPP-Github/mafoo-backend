package kr.mafoo.photo.domain;

import java.util.regex.Pattern;

public enum BrandType {
    LIFE_FOUR_CUTS(Pattern.compile("https://api\\.life4cut\\.net/.*")),
    PHOTOISM(Pattern.compile("https://qr\\.seobuk\\.kr/.*")),
    HARU_FILM(Pattern.compile("http://haru\\d+\\.mx\\d+\\.co\\.kr/.*")),
    DONT_LOOK_UP(Pattern.compile("https://x\\.dontlxxkup\\.kr/.*")),
    MY_FOUR_CUT(Pattern.compile("https://firebasestorage\\.googleapis\\.com:443/v0/b/my4ccu\\.appspot\\.com/.*")),
    PHOTOGRAY(Pattern.compile("https://pgshort\\.aprd\\.io/.*")),
    MONOMANSION(Pattern.compile("https://monomansion\\.net/.*")),
    PHOTO_SIGNATURE(Pattern.compile("http://photoqr3\\.kr/.*"))
    ;

    private final Pattern urlPattern;

    private BrandType(Pattern urlPattern) {
        this.urlPattern = urlPattern;
    }

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


package kr.mafoo.user.util;

import java.util.concurrent.ThreadLocalRandom;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ProfileImageGenerator {
    private static String[] profileImageUrls = new String[] {
        "https://kr.object.ncloudstorage.com/mafoo/src/mafoo_profile_blue.png",
        "https://kr.object.ncloudstorage.com/mafoo/src/mafoo_profile_pink.png",
        "https://kr.object.ncloudstorage.com/mafoo/src/mafoo_profile_purple.png",
        "https://kr.object.ncloudstorage.com/mafoo/src/mafoo_profile_yellow.png"
    };

    public static String generate() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return profileImageUrls[random.nextInt(profileImageUrls.length)];
    }

}

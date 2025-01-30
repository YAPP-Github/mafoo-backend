package kr.mafoo.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VariablePlaceholder {
    ME_NAME("name", "{{사용자 이름}}"),

    ALBUM_NAME("name","{{앨범 이름}}"),
    ALBUM_PHOTO_COUNT("photoCount", "{{앨범 사진 수}}"),
    ALBUM_OWNER_NAME("ownerMemberName", "{{앨범 소유자 이름}}"),

    SHARE_MEMBER_INVITE_STATUS("inviteStatus", "{{공유 앨범 사용자 초대 상태}}"),
    SHARE_MEMBER_NAME("name", "{{공유 앨범 사용자 이름}}"),

    SHARE_ALBUM_NAME("albumName", "{{공유 앨범 이름}}"),
    SHARE_ALBUM_PHOTO_COUNT("albumPhotoCount", "{{공유 앨범 사진 수}}"),
    SHARE_ALBUM_OWNER_NAME("albumOwnerMemberName", "{{공유 앨범 소유자 이름}}"),
    ;

    private final String placeholderKey;
    private final String placeholder;
}


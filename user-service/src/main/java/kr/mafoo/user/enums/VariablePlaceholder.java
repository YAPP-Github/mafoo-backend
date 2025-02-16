package kr.mafoo.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VariablePlaceholder {
    MEMBER_ID("memberId", "{{사용자 ID}}"),
    MEMBER_NAME("name", "{{사용자 이름}}"),
    MEMBER_PROFILE_IMAGE_URL("profileImageUrl", "{{공유 대상 사용자 프로필 URL}}"),
    MEMBER_SERIAL_NUMBER("serialNumber", "{{공유 대상 사용자 식별자}}"),

    ALBUM_ID("albumId", "{{앨범 ID}}"),
    ALBUM_NAME("name","{{앨범 이름}}"),
    ALBUM_TYPE("type", "{{앨범 종류}}"),
    ALBUM_PHOTO_COUNT("photoCount", "{{앨범 사진 수}}"),

    ALBUM_OWNER_MEMBER_ID("ownerMemberId", "{{앨범 소유자 ID}}"),
    ALBUM_OWNER_MEMBER_NAME("ownerName", "{{앨범 소유자 이름}}"),

    SHARED_MEMBER_ID("sharedMemberId", "{{공유 사용자 ID}}"),
    SHARED_MEMBER_SHARE_STATUS("shareStatus", "{{공유 사용자 초대 상태}}"),
    SHARED_MEMBER_PERMISSION_LEVEL("permissionLevel", "{{공유 사용자 권한 종류}}"),

    SHARED_MEMBER_MEMBER_ID("memberId", "{{공유 대상 사용자 ID}}"),
    SHARED_MEMBER_MEMBER_NAME("memberName", "{{공유 대상 사용자 이름}}"),
    SHARED_MEMBER_MEMBER_PROFILE_IMAGE_URL("memberProfileImageUrl", "{{공유 대상 사용자 프로필 URL}}"),
    SHARED_MEMBER_MEMBER_SERIAL_NUMBER("memberSerialNumber", "{{공유 대상 사용자 식별자}}"),

    SHARED_MEMBER_ALBUM_ID("albumId", "{{공유 대상 앨범 ID}}"),
    SHARED_MEMBER_ALBUM_NAME("albumName", "{{공유 대상 앨범 이름}}"),
    SHARED_MEMBER_ALBUM_TYPE("albumType", "{{공유 대상 앨범 종류}}"),
    SHARED_MEMBER_ALBUM_PHOTO_COUNT("albumPhotoCount", "{{공유 대상 앨범 사진 수}}")
    ;

    private final String key;
    private final String placeholder;
}


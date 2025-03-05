package kr.mafoo.user.enums.variables;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TextVariable implements Variable {
    // Notification
    SENDER_MEMBER_NAME("senderName", "{{발신자 사용자 이름}}"),
    RECEIVER_MEMBER_NAME("receiverName", "{{수신자 사용자 이름}}"),

    // Album
    ALBUM_NAME("albumName","{{앨범 이름}}"),
    ALBUM_PHOTO_COUNT("albumPhotoCount", "{{앨범 사진 수}}"),
    ALBUM_OWNER_MEMBER_NAME("ownerName", "{{앨범 소유자 이름}}"),

    // Shared Member
    SHARED_MEMBER_MEMBER_NAME("shareTargetMemberName", "{{공유 대상 사용자 이름}}"),
    SHARED_MEMBER_ALBUM_NAME("shareTargetAlbumName", "{{공유 대상 앨범 이름}}"),
    SHARED_MEMBER_ALBUM_PHOTO_COUNT("shareTargetAlbumPhotoCount", "{{공유 대상 앨범 사진 수}}"),
    SHARED_MEMBER_ALBUM_OWNER_MEMBER_NAME("shareTargetAlbumOwnerName", "{{공유 대상 앨범 소유자 이름}}"),
    ;

    private final String jsonKey;
    private final String placeholder;
}
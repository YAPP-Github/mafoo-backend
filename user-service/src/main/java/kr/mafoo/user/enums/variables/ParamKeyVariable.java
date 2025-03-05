package kr.mafoo.user.enums.variables;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ParamKeyVariable implements Variable {
    // Notification
    SENDER_MEMBER_ID("senderMemberId", "{{발신자 사용자 ID}}"),
    RECEIVER_MEMBER_ID("receiverMemberId", "{{수신자 사용자 ID}}"),

    // Album
    ALBUM_ID("albumId", "{{앨범 ID}}"),
    ALBUM_OWNER_MEMBER_ID("ownerMemberId", "{{앨범 소유자 ID}}"),

    // Shared Member
    SHARED_MEMBER_ID("sharedMemberId", "{{공유 사용자 ID}}"),
    SHARED_MEMBER_MEMBER_ID("shareTargetMemberId", "{{공유 대상 사용자 ID}}"),
    SHARED_MEMBER_ALBUM_ID("shareTargetAlbumId", "{{공유 대상 앨범 ID}}"),

    // Recap
    RECAP_URL("recapUrl", "{{리캡 URL}}"),
    ;

    private final String jsonKey;
    private final String placeholder;
}


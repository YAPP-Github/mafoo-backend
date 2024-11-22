package kr.mafoo.photo.service.dto;

import java.time.LocalDateTime;
import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.SharedMemberEntity;
import kr.mafoo.photo.domain.enums.AlbumType;
import kr.mafoo.photo.domain.enums.PermissionLevel;
import kr.mafoo.photo.domain.enums.ShareStatus;

public record AlbumDto(
    String albumId,
    String name,
    AlbumType type,
    Integer photoCount,
    Boolean isShared,
    ShareStatus shareStatus,
    PermissionLevel permissionLevel,
    String ownerProfileImgUrl,
    LocalDateTime createdAt
) {
    public static AlbumDto fromOwnedAlbum(
        AlbumEntity albumEntity
    ) {
        return new AlbumDto(
            albumEntity.getAlbumId(),
            albumEntity.getName(),
            albumEntity.getType(),
            albumEntity.getPhotoCount(),
            false,
            null,
            null,
            null,
            albumEntity.getCreatedAt()
        );
    }

    public static AlbumDto fromSharedAlbum(
        AlbumEntity albumEntity,
        SharedMemberEntity sharedMemberEntity,
        MemberDto memberDto
    ) {
        return new AlbumDto(
            albumEntity.getAlbumId(),
            albumEntity.getName(),
            albumEntity.getType(),
            albumEntity.getPhotoCount(),
            true,
            sharedMemberEntity.getShareStatus(),
            sharedMemberEntity.getPermissionLevel(),
            memberDto.profileImageUrl(),
            sharedMemberEntity.getCreatedAt()
        );
    }
}

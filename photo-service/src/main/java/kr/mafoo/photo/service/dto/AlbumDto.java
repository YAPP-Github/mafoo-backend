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
    String ownerMemberId,
    String ownerName,
    String ownerProfileImageUrl,
    String ownerSerialNumber,
    String sharedMemberId,
    ShareStatus shareStatus,
    PermissionLevel permissionLevel,
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
            albumEntity.getOwnerMemberId(),
            null,
            null,
            null,
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
            albumEntity.getOwnerMemberId(),
            memberDto.name(),
            memberDto.profileImageUrl(),
            memberDto.serialNumber(),
            sharedMemberEntity.getSharedMemberId(),
            sharedMemberEntity.getShareStatus(),
            sharedMemberEntity.getPermissionLevel(),
            sharedMemberEntity.getCreatedAt()
        );
    }
}

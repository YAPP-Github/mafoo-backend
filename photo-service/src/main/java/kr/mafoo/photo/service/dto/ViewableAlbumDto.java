package kr.mafoo.photo.service.dto;

import java.time.LocalDateTime;
import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.SharedMemberEntity;
import kr.mafoo.photo.domain.enums.AlbumType;
import kr.mafoo.photo.domain.enums.PermissionLevel;
import kr.mafoo.photo.domain.enums.ShareStatus;

public record ViewableAlbumDto(
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
    public static ViewableAlbumDto fromOwnedAlbum(
        AlbumEntity albumEntity,
        MemberDto ownerMemberDto
    ) {
        return new ViewableAlbumDto(
            albumEntity.getAlbumId(),
            albumEntity.getName(),
            albumEntity.getType(),
            albumEntity.getPhotoCount(),
            albumEntity.getOwnerMemberId(),
            ownerMemberDto.name(),
            ownerMemberDto.profileImageUrl(),
            ownerMemberDto.serialNumber(),
            null,
            null,
            null,
            albumEntity.getCreatedAt()
        );
    }

    public static ViewableAlbumDto fromSharedAlbum(
        AlbumEntity albumEntity,
        MemberDto ownerMemberDto,
        SharedMemberEntity sharedMemberEntity
    ) {
        return new ViewableAlbumDto(
            albumEntity.getAlbumId(),
            albumEntity.getName(),
            albumEntity.getType(),
            albumEntity.getPhotoCount(),
            albumEntity.getOwnerMemberId(),
            ownerMemberDto.name(),
            ownerMemberDto.profileImageUrl(),
            ownerMemberDto.serialNumber(),
            sharedMemberEntity.getSharedMemberId(),
            sharedMemberEntity.getShareStatus(),
            sharedMemberEntity.getPermissionLevel(),
            sharedMemberEntity.getCreatedAt()
        );
    }
}

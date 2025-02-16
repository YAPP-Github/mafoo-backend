package kr.mafoo.photo.service.dto;

import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.SharedMemberEntity;
import kr.mafoo.photo.domain.enums.AlbumType;
import kr.mafoo.photo.domain.enums.PermissionLevel;
import kr.mafoo.photo.domain.enums.ShareStatus;

public record SharedMemberDetailDto(
    String sharedMemberId,
    ShareStatus shareStatus,
    PermissionLevel permissionLevel,
    String memberId,
    String memberName,
    String memberProfileImageUrl,
    String memberSerialNumber,
    String albumId,
    String albumName,
    AlbumType albumType,
    String albumPhotoCount,
    String albumOwnerMemberId
) {
    public static SharedMemberDetailDto fromSharedMember(
        SharedMemberEntity sharedMemberEntity,
        MemberDto memberDto,
        AlbumEntity albumEntity
    ) {
        return new SharedMemberDetailDto(
            sharedMemberEntity.getSharedMemberId(),
            sharedMemberEntity.getShareStatus(),
            sharedMemberEntity.getPermissionLevel(),
            memberDto.memberId(),
            memberDto.name(),
            memberDto.profileImageUrl(),
            memberDto.serialNumber(),
            albumEntity.getAlbumId(),
            albumEntity.getName(),
            albumEntity.getType(),
            albumEntity.getPhotoCount().toString(),
            albumEntity.getOwnerMemberId()
        );
    }
}

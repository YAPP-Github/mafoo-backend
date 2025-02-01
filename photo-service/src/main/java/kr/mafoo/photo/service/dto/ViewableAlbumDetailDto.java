package kr.mafoo.photo.service.dto;

import java.util.List;
import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.enums.AlbumType;

public record ViewableAlbumDetailDto(
    String albumId,
    String name,
    AlbumType type,
    Integer photoCount,
    String ownerMemberId,
    String ownerName,
    String ownerProfileImageUrl,
    String ownerSerialNumber,
    List<SharedMemberDto> sharedMemberDtoList
) {
    public static ViewableAlbumDetailDto fromOwnedAlbum(
        AlbumEntity albumEntity
    ) {
        return new ViewableAlbumDetailDto(
            albumEntity.getAlbumId(),
            albumEntity.getName(),
            albumEntity.getType(),
            albumEntity.getPhotoCount(),
            albumEntity.getOwnerMemberId(),
            null,
             null,
            null,
            null
        );
    }

    public static ViewableAlbumDetailDto fromSharedAlbum(
        AlbumEntity albumEntity,
        MemberDto ownerMemberDto,
        List<SharedMemberDto> sharedMemberDtoList
    ) {
        return new ViewableAlbumDetailDto(
                albumEntity.getAlbumId(),
                albumEntity.getName(),
                albumEntity.getType(),
                albumEntity.getPhotoCount(),
                ownerMemberDto.memberId(),
                ownerMemberDto.name(),
                ownerMemberDto.profileImageUrl(),
                ownerMemberDto.serialNumber(),
                sharedMemberDtoList
        );
    }
}

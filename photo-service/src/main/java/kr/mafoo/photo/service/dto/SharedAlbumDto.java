package kr.mafoo.photo.service.dto;

import java.util.List;
import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.domain.enums.AlbumType;
import reactor.core.publisher.Flux;

public record SharedAlbumDto(
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
    public static SharedAlbumDto fromOwnedAlbum(
        AlbumEntity albumEntity
    ) {
        return new SharedAlbumDto(
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

    public static SharedAlbumDto fromSharedAlbum(
        AlbumEntity albumEntity,
        MemberDto ownerMemberDto,
        List<SharedMemberDto> sharedMemberDtoList
    ) {
        return new SharedAlbumDto(
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

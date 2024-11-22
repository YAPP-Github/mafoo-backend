package kr.mafoo.photo.service.dto;

import kr.mafoo.photo.domain.AlbumEntity;
import reactor.core.publisher.Flux;

public record SharedAlbumDto(
    String albumId,
    String ownerMemberId,
    String ownerName,
    String ownerProfileImageUrl,
    String ownerSerialNumber,
    Flux<SharedMemberDto> sharedMemberDtoFlux
) {
    public static SharedAlbumDto fromSharedAlbum(
        AlbumEntity albumEntity,
        MemberDto ownerMemberDto,
        Flux<SharedMemberDto> sharedMemberDtoFlux
    ) {
        return new SharedAlbumDto(
                albumEntity.getAlbumId(),
                ownerMemberDto.memberId(),
                ownerMemberDto.name(),
                ownerMemberDto.profileImageUrl(),
                ownerMemberDto.serialNumber(),
                sharedMemberDtoFlux
            );
    }
}

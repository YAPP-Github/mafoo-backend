package kr.mafoo.photo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.mafoo.photo.service.dto.SharedAlbumDto;
import reactor.core.publisher.Mono;

@Schema(description = "공유 앨범 응답")
public record SharedAlbumResponse(
        @Schema(description = "앨범 ID", example = "test_album_id")
        String albumId,

        @Schema(description = "공유 대상 사용자 ID", example = "test_member_id")
        String ownerMemberId,

        @Schema(description = "사용자 이름", example = "시금치파슷하")
        String ownerName,

        @Schema(description = "프로필 이미지 URL", example = "https://mafoo.kr/profile.jpg")
        String ownerProfileImageUrl,

        @Schema(description = "식별 번호", example = "0000")
        String ownerSerialNumber,

        @Schema(description = "공유 앨범 사용자 정보 목록")
        List<SharedMemberDetailResponse> sharedMembers
) {
        public static Mono<SharedAlbumResponse> fromDto(
            SharedAlbumDto dto
        ) {
            return dto.sharedMemberDtoFlux()
                .map(SharedMemberDetailResponse::fromDto)
                .collectList()
                .map(sharedMemberList -> new SharedAlbumResponse(
                    dto.albumId(),
                    dto.ownerMemberId(),
                    dto.ownerName(),
                    dto.ownerProfileImageUrl(),
                    dto.ownerSerialNumber(),
                    sharedMemberList
            ));
        }
}

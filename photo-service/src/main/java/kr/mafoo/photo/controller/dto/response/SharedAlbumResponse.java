package kr.mafoo.photo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.mafoo.photo.domain.enums.AlbumType;
import kr.mafoo.photo.service.dto.SharedAlbumDto;

@Schema(description = "공유 앨범 응답")
public record SharedAlbumResponse(
        @Schema(description = "앨범 ID", example = "test_album_id")
        String albumId,

        @Schema(description = "앨범 이름", example = "야뿌들")
        String name,

        @Schema(description = "앨범 종류", example = "HEART")
        AlbumType type,

        @Schema(description = "앨범 내 사진 수", example = "6")
        String photoCount,

        @Schema(description = "공유 앨범 소유자 ID", example = "test_member_id")
        String ownerMemberId,

        @Schema(description = "공유 앨범 소유자 이름", example = "시금치파슷하")
        String ownerName,

        @Schema(description = "공유 앨범 소유자 프로필 이미지 URL", example = "https://mafoo.kr/profile.jpg")
        String ownerProfileImageUrl,

        @Schema(description = "공유 앨범 소유자 식별 번호", example = "0000")
        String ownerSerialNumber,

        @Schema(description = "공유 앨범 사용자 정보 목록")
        List<SharedMemberDetailResponse> sharedMembers
) {
        public static SharedAlbumResponse fromDto(
            SharedAlbumDto dto
        ) {
                return new SharedAlbumResponse(
                    dto.albumId(),
                    dto.name(),
                    dto.type(),
                    dto.photoCount().toString(),
                    dto.ownerMemberId(),
                    dto.ownerName(),
                    dto.ownerProfileImageUrl(),
                    dto.ownerSerialNumber(),
                    dto.sharedMemberDtoList().stream().map(SharedMemberDetailResponse::fromDto).toList()
                );
        }
}

package kr.mafoo.photo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.mafoo.photo.domain.enums.AlbumType;
import kr.mafoo.photo.domain.enums.PermissionLevel;
import kr.mafoo.photo.domain.enums.ShareStatus;
import kr.mafoo.photo.service.dto.SharedMemberDetailDto;

@Schema(description = "공유 사용자 상세 응답")
public record SharedMemberDetailResponse(
        @Schema(description = "공유 사용자 ID", example = "test_shared_member_id")
        String sharedMemberId,

        @Schema(description = "공유 상태", example = "PENDING")
        ShareStatus shareStatus,

        @Schema(description = "권한 단계", example = "FULL_ACCESS")
        PermissionLevel permissionLevel,

        @Schema(description = "공유 대상 사용자 ID", example = "test_member_id")
        String memberId,

        @Schema(description = "사용자 이름", example = "시금치파슷하")
        String memberName,

        @Schema(description = "프로필 이미지 URL", example = "https://mafoo.kr/profile.jpg")
        String memberProfileImageUrl,

        @Schema(description = "식별 번호", example = "0000")
        String memberSerialNumber,

        @Schema(description = "앨범 ID", example = "test_album_id")
        String albumId,

        @Schema(description = "앨범 이름", example = "야뿌들")
        String albumName,

        @Schema(description = "앨범 종류", example = "TYPE_B")
        AlbumType albumType,

        @Schema(description = "앨범 내 사진 수", example = "6")
        String albumPhotoCount,

        @Schema(description = "앨범 소유자 ID", example = "6")
        String albumOwnerMemberId
) {
        public static SharedMemberDetailResponse fromDto(
            SharedMemberDetailDto dto
        ) {
            return new SharedMemberDetailResponse(
                dto.sharedMemberId(),
                dto.shareStatus(),
                dto.permissionLevel(),
                dto.memberId(),
                dto.memberName(),
                dto.memberProfileImageUrl(),
                dto.memberSerialNumber(),
                dto.albumId(),
                dto.albumName(),
                dto.albumType(),
                dto.albumPhotoCount(),
                dto.albumOwnerMemberId()
            );
        }
}

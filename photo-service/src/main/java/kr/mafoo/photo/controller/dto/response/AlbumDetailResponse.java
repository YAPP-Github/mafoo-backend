package kr.mafoo.photo.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import kr.mafoo.photo.domain.enums.AlbumType;
import kr.mafoo.photo.domain.enums.PermissionLevel;
import kr.mafoo.photo.domain.enums.ShareStatus;
import kr.mafoo.photo.service.dto.AlbumDto;

@Schema(description = "앨범 응답")
public record AlbumDetailResponse(
        @Schema(description = "앨범 ID", example = "test_album_id")
        String albumId,

        @Schema(description = "앨범 이름", example = "야뿌들")
        String name,

        @Schema(description = "앨범 종류", example = "HEART")
        AlbumType type,

        @Schema(description = "앨범 내 사진 수", example = "6")
        String photoCount,

        @Schema(description = "공유 받은 앨범 상태", example = "null")
        ShareStatus shareStatus,

        @Schema(description = "공유 받은 앨범 권한", example = "null")
        PermissionLevel permissionLevel,

        @Schema(description = "공유 받은 앨범 소유자 ID", example = "test_member_id")
        String ownerMemberId,

        @Schema(description = "공유 받은 앨범 소유자 이름", example = "시금치파슷하")
        String ownerName,

        @Schema(description = "공유 받은 앨범 소유자 프로필 사진 URL", example = "null")
        String ownerProfileImageUrl,

        @Schema(description = "공유 받은 앨범 소유자 식별 번호", example = "0000")
        String ownerSerialNumber,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        @Schema(description = "생성 시간", example = "2021-08-01 00:00:00")
        LocalDateTime createdAt
) {
        public static AlbumDetailResponse fromDto(
            AlbumDto dto
        ) {
                return new AlbumDetailResponse(
                    dto.albumId(),
                    dto.name(),
                    dto.type(),
                    dto.photoCount().toString(),
                    dto.shareStatus(),
                    dto.permissionLevel(),
                    dto.ownerMemberId(),
                    dto.ownerName(),
                    dto.ownerProfileImageUrl(),
                    dto.ownerSerialNumber(),
                    dto.createdAt()
                );
        }
}

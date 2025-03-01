package kr.mafoo.user.service.dto;

import java.util.Optional;
import kr.mafoo.user.domain.FcmTokenEntity;
import kr.mafoo.user.domain.MemberEntity;

public record MeDto(
    String memberId,
    String name,
    boolean isDefaultName,
    String profileImageUrl,
    String serialNumber,
    String fcmToken
) {
    public static MeDto fromEntities(
        MemberEntity memberEntity,
        FcmTokenEntity fcmTokenEntity
    ) {
        return new MeDto(
            memberEntity.getId(),
            memberEntity.getName(),
            memberEntity.isDefaultName(),
            memberEntity.getProfileImageUrl(),
            String.format("%04d", memberEntity.getSerialNumber()),
            fcmTokenEntity.getToken()
        );
    }

    public static MeDto fromEntities(
        MemberEntity memberEntity
    ) {
        return new MeDto(
            memberEntity.getId(),
            memberEntity.getName(),
            memberEntity.isDefaultName(),
            memberEntity.getProfileImageUrl(),
            String.format("%04d", memberEntity.getSerialNumber()),
            null
        );
    }
}

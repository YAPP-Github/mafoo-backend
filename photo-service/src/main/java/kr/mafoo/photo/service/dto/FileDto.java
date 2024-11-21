package kr.mafoo.photo.service.dto;

import kr.mafoo.photo.domain.enums.BrandType;

public record FileDto (
        BrandType type,
        byte[] fileByte
) {
}

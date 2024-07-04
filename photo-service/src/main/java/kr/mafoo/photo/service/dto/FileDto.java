package kr.mafoo.photo.service.dto;

import kr.mafoo.photo.domain.BrandType;

public record FileDto (
        BrandType type,
        byte[] fileByte
) {
}

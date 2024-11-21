package kr.mafoo.photo.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionLevel {
    FULL_ACCESS(3),     // 전체 권한
    DOWNLOAD_ACCESS(2), // 다운로드 권한
    VIEW_ACCESS(1),     // 뷰 권한
    ;

    private final int tier;
}

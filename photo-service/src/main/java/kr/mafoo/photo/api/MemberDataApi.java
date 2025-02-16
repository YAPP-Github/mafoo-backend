package kr.mafoo.photo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.mafoo.photo.annotation.RequestMemberId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@Validated
@Tag(name = "사용자 데이터 API", description = "회원 탈퇴 시 사용하는 API")
@RequestMapping("/v1/member-data")
public interface MemberDataApi {

    @Operation(summary = "사용자 데이터 삭제", description = "사용자와 관련된 앨범, 사진, 공유 사용자 데이터를 삭제합니다.")
    @DeleteMapping
    Flux<Void> deleteMemberData(
            @RequestMemberId
            String memberId
    );
}

package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.MemberDataApi;
import kr.mafoo.photo.service.MemberDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
public class MemberDataController implements MemberDataApi {

    private final MemberDataService memberDataService;

    @Override
    public Flux<Void> deleteMemberData(
            String memberId
    ){
        return memberDataService.removeMemberData(memberId);
    }
}

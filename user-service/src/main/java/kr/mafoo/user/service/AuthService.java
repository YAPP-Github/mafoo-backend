package kr.mafoo.user.service;

import kr.mafoo.user.controller.dto.response.KakaoLoginInfo;
import kr.mafoo.user.domain.AuthToken;
import kr.mafoo.user.domain.SocialMemberEntity;
import kr.mafoo.user.domain.SocialMemberEntityKey;
import kr.mafoo.user.enums.IdentityProvider;
import kr.mafoo.user.repository.SocialMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final WebClient externalWebClient;
    private final SocialMemberRepository socialMemberRepository;
    private final MemberService memberService;


    public Mono<AuthToken> loginWithKakao(String code) {
        return getKakaoTokenWithCode(code)
                .flatMap(this::getUserInfoWithKakaoToken)
                .flatMap(kakaoLoginInfo -> getOrCreateMember(IdentityProvider.KAKAO, kakaoLoginInfo.id()));
    }

    private Mono<AuthToken> generateTokenWithMemberId(String memberId) {
        return Mono.empty();
    }

    private Mono<AuthToken> getOrCreateMember(IdentityProvider provider, String id) {
        SocialMemberEntityKey key = new SocialMemberEntityKey(provider, id);
        return socialMemberRepository
                .findById(key)
                .switchIfEmpty(createNewSocialMember(provider, id))
                .flatMap(socialMember -> generateTokenWithMemberId(socialMember.getMemberId()));
    }

    private Mono<SocialMemberEntity> createNewSocialMember(IdentityProvider provider, String id) {
        return memberService
                .createNewMember(id)
                .flatMap(newMember -> socialMemberRepository.save(
                        SocialMemberEntity.newSocialMember(provider, id, newMember.getId())
                ));
    }

    /**
     * 카카오 로그인 관련 로직
     */
    private Mono<String> getKakaoTokenWithCode(String code) {
        return externalWebClient
                .post()
                .uri("https://kauth.kakao.com/oauth/token")
                .bodyValue("grant_type=authorization_code&client_id=client_id&redirect_uri=http://localhost:8080/login/kakao&code=" + code)
                .retrieve()
                .bodyToMono(LinkedHashMap.class)
                .map(map -> (String) map.get("access_token"));
    }

    private Mono<KakaoLoginInfo> getUserInfoWithKakaoToken(String kakaoToken){
        return externalWebClient
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(headers -> headers.setBearerAuth(kakaoToken))
                .retrieve()
                .bodyToMono(LinkedHashMap.class)
                .map(map -> new KakaoLoginInfo(
                        (String) map.get("id"),
                        (String) map.get("kakao_account.email"),
                        (String) map.get("properties.nickname"))
                );
    }
}

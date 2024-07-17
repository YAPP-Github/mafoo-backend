package kr.mafoo.user.service;

import kr.mafoo.user.config.properties.KakaoOAuthProperties;
import kr.mafoo.user.controller.dto.response.KakaoLoginInfo;
import kr.mafoo.user.domain.AuthToken;
import kr.mafoo.user.domain.SocialMemberEntity;
import kr.mafoo.user.enums.IdentityProvider;
import kr.mafoo.user.exception.KakaoLoginFailedException;
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
    private final JWTTokenService jwtTokenService;
    private final KakaoOAuthProperties kakaoOAuthProperties;


    public Mono<AuthToken> loginWithKakao(String code) {
        return getKakaoTokenWithCode(code)
                .flatMap(this::getUserInfoWithKakaoToken)
                .flatMap(kakaoLoginInfo -> getOrCreateMember(
                        IdentityProvider.KAKAO,
                        kakaoLoginInfo.id(),
                        kakaoLoginInfo.nickname(),
                        kakaoLoginInfo.profileImageUrl()
                ));
    }

    public Mono<AuthToken> loginWithApple(String code) {
        return Mono.empty();
    }

    public Mono<AuthToken> loginWithRefreshToken(String refreshToken){
        return Mono
                .fromCallable(() -> jwtTokenService.extractUserIdFromRefreshToken(refreshToken))
                .map(memberId -> {
                    String accessToken = jwtTokenService.generateAccessToken(memberId);
                    String newRefreshToken = jwtTokenService.generateRefreshToken(memberId);
                    return new AuthToken(accessToken, newRefreshToken);
                });
    }

    private Mono<AuthToken> getOrCreateMember(IdentityProvider provider, String id, String username, String profileImageUrl) {
        return socialMemberRepository
                .findByIdentityProviderAndId(provider, id)
                .switchIfEmpty(createNewSocialMember(provider, id, username, profileImageUrl))
                .map(socialMember -> {
                    String accessToken = jwtTokenService.generateAccessToken(socialMember.getMemberId());
                    String refreshToken = jwtTokenService.generateRefreshToken(socialMember.getMemberId());
                    return new AuthToken(accessToken, refreshToken);
                });
    }

    private Mono<SocialMemberEntity> createNewSocialMember(IdentityProvider provider, String id, String username, String profileImageUrl) {
        return memberService
                .createNewMember(username, profileImageUrl)
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
                .uri("https://kauth.kakao.com/oauth/token" + "?grant_type=authorization_code&client_id="
                        + kakaoOAuthProperties.clientId()
                        + "&redirect_uri="
                        + kakaoOAuthProperties.redirectUri()
                        +"&code="
                        + code
                        + "&client_secret="
                        + kakaoOAuthProperties.clientSecret()
                )
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), (res) -> Mono.error(new KakaoLoginFailedException()))
                .bodyToMono(LinkedHashMap.class)
                .map(map -> (String) map.get("access_token"));
    }

    private Mono<KakaoLoginInfo> getUserInfoWithKakaoToken(String kakaoToken){
        return externalWebClient
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(headers -> headers.setBearerAuth(kakaoToken))
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), (res) -> Mono.error(new KakaoLoginFailedException()))
                .bodyToMono(LinkedHashMap.class)
                .map(map -> new KakaoLoginInfo(
                                String.valueOf(map.get("id")),
                                (String) ((LinkedHashMap)map.get("properties")).get("nickname"),
                                (String) map.get("kakao_account.email"),
                        (String) ((LinkedHashMap)map.get("properties")).get("profile_image")
                ));
    }
}

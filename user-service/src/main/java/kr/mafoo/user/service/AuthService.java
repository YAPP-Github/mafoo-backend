package kr.mafoo.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWK;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Set;
import kr.mafoo.user.config.properties.AppleOAuthProperties;
import kr.mafoo.user.config.properties.KakaoOAuthProperties;
import kr.mafoo.user.controller.dto.response.AppleKeyListResponse;
import kr.mafoo.user.controller.dto.response.AppleKeyResponse;
import kr.mafoo.user.controller.dto.response.AppleLoginInfo;
import kr.mafoo.user.controller.dto.response.KakaoLoginInfo;
import kr.mafoo.user.domain.AuthToken;
import kr.mafoo.user.domain.SocialMemberEntity;
import kr.mafoo.user.enums.IdentityProvider;
import kr.mafoo.user.exception.KakaoLoginFailedException;
import kr.mafoo.user.repository.SocialMemberRepository;
import kr.mafoo.user.util.NicknameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final WebClient externalWebClient;
    private final SocialMemberRepository socialMemberRepository;
    private final MemberService memberService;
    private final JWTTokenService jwtTokenService;
    private final KakaoOAuthProperties kakaoOAuthProperties;
    private final AppleOAuthProperties appleOAuthProperties;
    private final ObjectMapper objectMapper;

    private final static String DEFAULT_PROFILE_IMG_URL = "https://www.gravatar.com/avatar/?d=identicon";

    @Transactional
    public Mono<AuthToken> loginWithKakao(String kakaoAccessToken, String userAgent) {
        return getUserInfoWithKakaoToken(kakaoAccessToken)
                .flatMap(kakaoLoginInfo -> getOrCreateMember(
                        IdentityProvider.KAKAO,
                        kakaoLoginInfo.id(),
                        kakaoLoginInfo.nickname(),
                        kakaoLoginInfo.profileImageUrl(),
                        userAgent
                ));
    }

    @Transactional
    public Mono<AuthToken> loginWithApple(String identityToken, String userAgent) {
        return getApplePublicKeys()
                .flatMap(keyObj -> getUserInfoWithAppleAccessToken(keyObj.keys(), identityToken))
                .flatMap(appleLoginInfo -> getOrCreateMember(
                        IdentityProvider.APPLE,
                        appleLoginInfo.id(),
                        NicknameGenerator.generate(),
                        DEFAULT_PROFILE_IMG_URL,
                        userAgent
                ));
    }

    @Transactional
    public Mono<AuthToken> loginWithRefreshToken(String refreshToken){
        return Mono
                .fromCallable(() -> jwtTokenService.extractUserIdFromRefreshToken(refreshToken))
                .map(memberId -> {
                    String accessToken = jwtTokenService.generateAccessToken(memberId);
                    String newRefreshToken = jwtTokenService.generateRefreshToken(memberId);
                    return new AuthToken(accessToken, newRefreshToken);
                });
    }

    private Mono<AuthToken> getOrCreateMember(IdentityProvider provider, String id, String username, String profileImageUrl, String userAgent) {
        return socialMemberRepository
                .findByIdentityProviderAndId(provider, id)
                .switchIfEmpty(createNewSocialMember(provider, id, username, profileImageUrl, userAgent))
                .map(socialMember -> {
                    String accessToken = jwtTokenService.generateAccessToken(socialMember.getMemberId());
                    String refreshToken = jwtTokenService.generateRefreshToken(socialMember.getMemberId());
                    return new AuthToken(accessToken, refreshToken);
                });
    }

    private Mono<SocialMemberEntity> createNewSocialMember(IdentityProvider provider, String id, String username, String profileImageUrl, String userAgent) {
        return memberService
                .createNewMember(username, profileImageUrl, userAgent)
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

    private Mono<AppleKeyListResponse> getApplePublicKeys(){
        return externalWebClient
                .get()
                .uri("https://appleid.apple.com/auth/keys")
                .retrieve()
                .bodyToMono(AppleKeyListResponse.class);
    }

    private Mono<AppleLoginInfo> getUserInfoWithAppleAccessToken(AppleKeyResponse[] keys, String identityToken) {
        return Mono.fromCallable(() -> {
            String[] tokenParts = identityToken.split("\\.");
            String headerPart = new String(Base64.getDecoder().decode(tokenParts[0]));
            JsonNode headerNode = objectMapper.readTree(headerPart);
            String kid = headerNode.get("kid").asText();
            AppleKeyResponse keyStr = Arrays.stream(keys)
                    .filter(k -> k.kid().equals(kid))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Apple Key not found"));

            RSAPublicKey pubKey = JWK.parse(objectMapper.writeValueAsString(keyStr)).toRSAKey().toRSAPublicKey();
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(pubKey)
                    .build()
                    .parseSignedClaims(identityToken);

            Set<String> audienceList = claims.getPayload().get("aud", Set.class);
            if (audienceList == null || !audienceList.contains(appleOAuthProperties.clientId())) {
                throw new RuntimeException();
            }

            return new AppleLoginInfo(claims.getPayload().get("sub", String.class));
        });
    }
}

package kr.mafoo.user.service;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import kr.mafoo.user.config.JacksonSerializer;
import kr.mafoo.user.exception.InvalidTokenException;
import kr.mafoo.user.exception.TokenExpiredException;
import kr.mafoo.user.exception.TokenTypeMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class JWTTokenService {
    @Value("${app.jwt.verify-key}")
    private String verifyKey;
    @Value("${app.jwt.expiration.access-token}")
    private Long accessTokenExpiration;
    @Value("${app.jwt.expiration.refresh-token}")
    private Long refreshTokenExpiration;



    private SecretKey signKey = null;
    private JwtParser parser = null;

    private JacksonSerializer jacksonSerializer = new JacksonSerializer();

    @PostConstruct
    public void initSignKey() {
        signKey = new SecretKeySpec(verifyKey.getBytes(), "AES");
        parser = Jwts
                .parser()
                .decryptWith(signKey)
                .build();
    }

    private final static String TOKEN_TYPE_HEADER_KEY = "tkn_typ";
    private final static String ACCESS_TOKEN_TYPE_VALUE = "access";
    private final static String REFRESH_TOKEN_TYPE_VALUE = "refresh";
    private final static String USER_ID_CLAIM_KEY = "user_id";

    public String generateAccessToken(String memberId) {
        return Jwts
                .builder()
                .header()
                .add(TOKEN_TYPE_HEADER_KEY, ACCESS_TOKEN_TYPE_VALUE)
                .and()
                .claims().add(USER_ID_CLAIM_KEY, memberId)
                .and()
                .expiration(generateAccessTokenExpirationDate())
                .encryptWith(signKey, Jwts.ENC.A128CBC_HS256)
                .json(jacksonSerializer)
                .compact();
    }

    public String generateRefreshToken(String memberId) {
        return Jwts
                .builder()
                .header()
                .add(TOKEN_TYPE_HEADER_KEY, REFRESH_TOKEN_TYPE_VALUE)
                .and()
                .claims().add(USER_ID_CLAIM_KEY, memberId)
                .and()
                .expiration(generateRefreshTokenExpirationDate())
                .encryptWith(signKey, Jwts.ENC.A128CBC_HS256)
                .json(jacksonSerializer)
                .compact();
    }

    public String extractUserIdFromRefreshToken(String refreshToken){
        Jwe<Claims> claims;
        try {
             claims = parser
                    .parseEncryptedClaims(refreshToken);
        } catch(ExpiredJwtException e){
            throw new TokenExpiredException();
        } catch (Exception e){
            throw new InvalidTokenException();
        }

        String type = (String) claims.getHeader().get(TOKEN_TYPE_HEADER_KEY);
        if (!type.equals(REFRESH_TOKEN_TYPE_VALUE)){
            throw new TokenTypeMismatchException();
        }

        return claims.getPayload().get(USER_ID_CLAIM_KEY, String.class);
    }

    private Date generateAccessTokenExpirationDate() {
        return new Date(System.currentTimeMillis() + 1000 * accessTokenExpiration);
    }

    public Date generateRefreshTokenExpirationDate() {
        return new Date(System.currentTimeMillis() + 1000 * refreshTokenExpiration);
    }
}

package skatn.remindmeback.common.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import skatn.remindmeback.common.exception.AuthException;
import skatn.remindmeback.common.exception.ErrorCode;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes());
    }

    public TokenDto generateTokens(Long memberId) {
        return new TokenDto(generateAccessToken(memberId), generateRefreshToken());
    }

    public String generateAccessToken(Long memberId) {
        Date expireDate = new Date(new Date().getTime() + jwtProperties.accessTokenExpireTime());

        return Jwts.builder()
                .issuer("Remind me")
                .issuedAt(new Date())
                .expiration(expireDate)
                .subject("AccessToken")
                .claim("memberId", memberId)
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken() {
        Date expireDate = new Date(new Date().getTime() + jwtProperties.refreshTokenExpireTime());

        return Jwts.builder()
                .issuer("Remind me")
                .issuedAt(new Date())
                .expiration(expireDate)
                .subject("RefreshToken")
                .signWith(secretKey)
                .compact();
    }

    public long getMemberIdFromAccessToken(String token) {
        validateToken(token);
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload()
                .get("memberId", Long.class);
    }

    public Date getExpiration(String token) {
        validateToken(token);
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload().getExpiration();
    }

    public void validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new AuthException(ErrorCode.JWT_INVALID_SIGNATURE);
        } catch (ExpiredJwtException e) {
            throw new AuthException(ErrorCode.JWT_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new AuthException(ErrorCode.JWT_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            throw new AuthException(ErrorCode.JWT_INVALID);
        }
    }

}

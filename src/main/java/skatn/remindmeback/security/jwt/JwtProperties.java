package skatn.remindmeback.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("auth.jwt")
public record JwtProperties(
        String secret,
        long accessTokenExpireTime,
        long refreshTokenExpireTime
) {
}

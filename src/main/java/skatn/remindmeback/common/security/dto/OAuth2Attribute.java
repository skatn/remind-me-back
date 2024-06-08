package skatn.remindmeback.common.security.dto;

import lombok.Builder;
import lombok.Getter;
import skatn.remindmeback.common.exception.AuthException;
import skatn.remindmeback.common.exception.ErrorCode;

import java.util.Map;

@Getter
@Builder
public class OAuth2Attribute {
    private String provider;

    private String username;
    private String name;

    public static OAuth2Attribute of(String provider, Map<String, Object> attributes) {
        return switch (provider) {
            case "kakao" -> ofKakao(provider, attributes);
            case "naver" -> ofNaver(provider, attributes);
            case "google" -> ofGoogle(provider, attributes);
            default -> throw new AuthException(ErrorCode.INVALID_OAUTH_PROVIDER);
        };
    }

    private static OAuth2Attribute ofKakao(String provider, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");


        return OAuth2Attribute.builder()
                .provider(provider)
//                .username((String) kakaoAccount.get("email"))
                .username((String) kakaoProfile.get("nickname")) // email을 가져오려면 비즈 앱으로 등록해야 되서 임시로 nickname 사용
                .name((String) kakaoProfile.get("nickname"))
                .build();
    }
    private static OAuth2Attribute ofNaver(String provider, Map<String, Object> attributes) {
        attributes = (Map<String, Object>) attributes.get("response");

        return OAuth2Attribute.builder()
                .provider(provider)
//                .username((String) attributes.get("email"))
                .username((String) attributes.get("nickname"))
                .name((String) attributes.get("nickname"))
                .build();
    }
    private static OAuth2Attribute ofGoogle(String provider, Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .provider(provider)
                .username((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .build();
    }
}

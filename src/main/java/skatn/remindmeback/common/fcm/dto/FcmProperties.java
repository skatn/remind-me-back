package skatn.remindmeback.common.fcm.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("firebase.messaging")
public record FcmProperties(
        String configPath,
        String sendApiUrl,
        String authUrl
) {
}

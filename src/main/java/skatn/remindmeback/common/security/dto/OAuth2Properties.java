package skatn.remindmeback.common.security.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("auth.oauth")
public record OAuth2Properties(String successRedirectUrl, String failureRedirectUrl) {
}

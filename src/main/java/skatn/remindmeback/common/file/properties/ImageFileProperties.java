package skatn.remindmeback.common.file.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("file.image")
public record ImageFileProperties(String path, String requestUrl) {
}

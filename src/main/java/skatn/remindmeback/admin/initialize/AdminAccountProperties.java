package skatn.remindmeback.admin.initialize;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("admin.account")
public record AdminAccountProperties (String username, String password, String name){
}

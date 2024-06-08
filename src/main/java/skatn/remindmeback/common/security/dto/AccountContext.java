package skatn.remindmeback.common.security.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class AccountContext implements UserDetails, OAuth2User {

    private final AccountDto accountDto;
    private Map<String, Object> attributes;

    public AccountContext(AccountDto accountDto) {
        this.accountDto = accountDto;
    }

    public AccountContext(AccountDto accountDto, Map<String, Object> attributes) {
        this.accountDto = accountDto;
        this.attributes = attributes;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(accountDto.role()));
    }

    @Override
    public String getPassword() {
        return accountDto.password();
    }

    @Override
    public String getUsername() {
        return accountDto.username();
    }

    @Override
    public boolean isEnabled() {
        return accountDto.isActive();
    }

    @Override
    public String getName() {
        return accountDto.provider();
    }
}

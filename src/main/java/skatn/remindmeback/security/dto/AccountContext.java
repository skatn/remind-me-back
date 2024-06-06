package skatn.remindmeback.security.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class AccountContext implements UserDetails {

    private final AccountDto accountDto;

    public AccountContext(AccountDto accountDto) {
        this.accountDto = accountDto;
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
}

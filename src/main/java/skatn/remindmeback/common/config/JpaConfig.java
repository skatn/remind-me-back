package skatn.remindmeback.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import skatn.remindmeback.common.security.dto.AccountDto;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    @Bean
    public AuditorAware<Long> getAuthUserIdAuditor() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext()
                    .getAuthentication();

            if(authentication == null) {
                return Optional.empty();
            }

            Object principal = authentication
                    .getPrincipal();

            return principal instanceof AccountDto ?
                    Optional.of(((AccountDto) principal).id()) :
                    Optional.empty();
        };
    }
}

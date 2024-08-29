package skatn.remindmeback.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import skatn.remindmeback.common.security.dto.AccountContext;
import skatn.remindmeback.common.security.dto.AccountDto;
import skatn.remindmeback.common.security.token.RestAuthenticationToken;

public class WithRestMockUserSecurityContextFactory implements WithSecurityContextFactory<WithRestMockUser> {
    @Override
    public SecurityContext createSecurityContext(WithRestMockUser annotation) {
        AccountContext accountContext = new AccountContext(new AccountDto(
                annotation.id(),
                annotation.name(),
                annotation.username(),
                null,
                "ROLE_USER",
                true,
                null
        ));

        Authentication authentication = new RestAuthenticationToken(accountContext.getAccountDto(), null, accountContext.getAuthorities());
        SecurityContext context = SecurityContextHolder.getContextHolderStrategy().createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}

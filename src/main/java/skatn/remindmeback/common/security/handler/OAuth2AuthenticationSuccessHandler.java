package skatn.remindmeback.common.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import skatn.remindmeback.common.security.dto.AccountContext;
import skatn.remindmeback.common.security.dto.OAuth2Properties;
import skatn.remindmeback.common.security.jwt.JwtProvider;
import skatn.remindmeback.common.security.jwt.TokenDto;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final OAuth2Properties oAuth2Properties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        AccountContext accountDto = (AccountContext) authentication.getPrincipal();

        TokenDto tokens = jwtProvider.generateTokens(accountDto.getAccountDto().id());

        String redirectUrl = UriComponentsBuilder.fromUriString(oAuth2Properties.successRedirectUrl())
                .queryParam("accessToken", tokens.accessToken())
                .queryParam("refreshToken", tokens.refreshToken())
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}

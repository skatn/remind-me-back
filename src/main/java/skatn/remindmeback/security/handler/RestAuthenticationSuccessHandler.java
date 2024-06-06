package skatn.remindmeback.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import skatn.remindmeback.security.dto.AccountDto;
import skatn.remindmeback.security.jwt.JwtProvider;
import skatn.remindmeback.security.jwt.TokenDto;
import skatn.remindmeback.security.service.RefreshTokenService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        AccountDto accountDto = (AccountDto) authentication.getPrincipal();

        TokenDto tokenDto = jwtProvider.generateTokens(accountDto.id());
        refreshTokenService.save(tokenDto.refreshToken(), accountDto.id());

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        mapper.writeValue(response.getWriter(), tokenDto);
    }
}

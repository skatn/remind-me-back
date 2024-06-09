package skatn.remindmeback.common.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import skatn.remindmeback.common.exception.AuthException;
import skatn.remindmeback.common.exception.BaseException;
import skatn.remindmeback.common.exception.EntityNotFoundException;
import skatn.remindmeback.common.exception.ErrorCode;
import skatn.remindmeback.common.security.dto.AccountContext;
import skatn.remindmeback.member.entity.Member;
import skatn.remindmeback.member.repository.MemberRepository;
import skatn.remindmeback.common.security.dto.AccountDto;
import skatn.remindmeback.common.security.jwt.JwtProvider;
import skatn.remindmeback.common.security.token.RestAuthenticationToken;

import java.io.IOException;

public class RestAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    public RestAuthorizationFilter(AuthenticationManager authenticationManager,
                                   JwtProvider jwtProvider,
                                   MemberRepository memberRepository) {

        super(authenticationManager);
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String accessToken = getAccessTokenFromHeader(request);

            long memberId = jwtProvider.getMemberIdFromAccessToken(accessToken);
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

            AccountDto accountDto = new AccountDto(member);
            AccountContext accountContext = new AccountContext(accountDto);

            RestAuthenticationToken restAuthenticationToken = new RestAuthenticationToken(accountContext.getAccountDto(), null, accountContext.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(restAuthenticationToken);
        } catch (BaseException e) {
            request.setAttribute("exception", e.getErrorCode().name());
        } catch (Exception e) {
            request.setAttribute("exception", ErrorCode.UN_AUTHORIZE.name());
        }

        chain.doFilter(request, response);
    }

    private String getAccessTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer")) {
            throw new AuthException(ErrorCode.PERMISSION_DENIED);
        }

        return authorizationHeader.substring(7);
    }
}

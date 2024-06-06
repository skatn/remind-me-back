package skatn.remindmeback.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import skatn.remindmeback.member.repository.MemberRepository;
import skatn.remindmeback.security.dsl.RestApiDsl;
import skatn.remindmeback.security.entrypoint.RestAuthenticationEntryPoint;
import skatn.remindmeback.security.filter.RestAuthorizationFilter;
import skatn.remindmeback.security.handler.RestAccessDeniedHandler;
import skatn.remindmeback.security.handler.RestAuthenticationFailureHandler;
import skatn.remindmeback.security.handler.RestAuthenticationSuccessHandler;
import skatn.remindmeback.security.jwt.JwtProvider;
import skatn.remindmeback.security.provider.RestAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final RestAuthenticationProvider authenticationProvider;
    private final RestAuthenticationSuccessHandler authenticationSuccessHandler;
    private final RestAuthenticationFailureHandler authenticationFailureHandler;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Bean
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilter(new RestAuthorizationFilter(authenticationManager, jwtProvider, memberRepository))
                .authenticationManager(authenticationManager)
                .with(new RestApiDsl<>(), restDsl -> restDsl
                        .restSuccessHandler(authenticationSuccessHandler)
                        .restFailureHandler(authenticationFailureHandler)
                        .loginProcessingUrl("/api/login")
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                        .accessDeniedHandler(new RestAccessDeniedHandler())
                )
        ;

        return http.build();
    }
}

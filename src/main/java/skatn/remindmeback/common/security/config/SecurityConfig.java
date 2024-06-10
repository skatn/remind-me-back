package skatn.remindmeback.common.security.config;

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
import skatn.remindmeback.common.security.dsl.RestApiDsl;
import skatn.remindmeback.common.security.entrypoint.RestAuthenticationEntryPoint;
import skatn.remindmeback.common.security.filter.RestAuthorizationFilter;
import skatn.remindmeback.common.security.handler.*;
import skatn.remindmeback.common.security.jwt.JwtProvider;
import skatn.remindmeback.common.security.provider.RestAuthenticationProvider;
import skatn.remindmeback.common.security.service.CustomOAuth2UserService;
import skatn.remindmeback.member.repository.MemberRepository;

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
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;


    @Bean
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().authenticated()
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
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler)
                        .userInfoEndpoint(endpoint -> endpoint
                                .userService(customOAuth2UserService)
                        )
                )
        ;

        return http.build();
    }

}

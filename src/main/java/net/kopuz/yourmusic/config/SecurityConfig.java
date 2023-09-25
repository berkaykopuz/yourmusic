package net.kopuz.yourmusic.config;

import net.kopuz.yourmusic.service.OAuthService;
import net.kopuz.yourmusic.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final OAuthConfig oAuthConfig;

    public SecurityConfig(OAuthConfig oAuthConfig) {
        this.oAuthConfig = oAuthConfig;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint
                                                .userService(oAuthConfig.oauth2UserService())
                                )
                )
                .csrf().disable()
                .build();
    }




}

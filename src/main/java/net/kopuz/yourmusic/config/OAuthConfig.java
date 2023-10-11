package net.kopuz.yourmusic.config;

import net.kopuz.yourmusic.service.OAuthService;
import net.kopuz.yourmusic.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class OAuthConfig {

    private final OAuthService oAuthService;
    private final UserService userService;

    public OAuthConfig(OAuthService oAuthService, UserService userService) {
        this.oAuthService = oAuthService;
        this.userService = userService;
    }

    @Bean //Configured for github. Google is not available now.
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return (userRequest) -> {
            OAuth2User oAuth2User = delegate.loadUser(userRequest);
            String oauthProvider = userRequest.getClientRegistration().getRegistrationId();
            String token = userRequest.getAccessToken().getTokenValue().toString();

            if("google".equals(oauthProvider)){

            }
            else if("github".equals(oauthProvider)){
                String oauthUserId = oAuth2User.getAttribute("id").toString();
                String username = oAuth2User.getAttribute("login");
                String email = oAuth2User.getAttribute("email");


                if(email==null){
                    email = oAuthService.getEmailFromGithub(token);
                }

                userService.saveOAuthUser(username, email, oauthUserId, oauthProvider);

            }

            Set<OAuth2UserAuthority> authorities = new HashSet<>();
            authorities.add(new OAuth2UserAuthority(oAuth2User.getAttributes()));

            return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), "login");

        };
    }
}

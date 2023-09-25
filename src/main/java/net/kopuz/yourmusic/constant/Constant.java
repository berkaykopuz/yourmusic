package net.kopuz.yourmusic.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constant {
    public static String clientId;
    public static String clientSecret;

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    public void setClientId(String clientId) {
        Constant.clientId = clientId;
    }
    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    public void setClientSecret(String clientSecret) {
        Constant.clientSecret = clientSecret;
    }


}

package net.kopuz.yourmusic.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.kopuz.yourmusic.constant.Constant;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Service
public class OAuthService {
    ObjectMapper objectMapper = new ObjectMapper();

    public String getEmailFromGithub(String token){
        String apiUrl = "https://api.github.com/user/emails";
        String oauthToken = token;
        String email = "";

        try {

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + oauthToken);
            connection.setRequestProperty("Accept", "application/json");

            InputStream responseStream = connection.getInputStream();
            Scanner scanner = new Scanner(responseStream, "UTF-8");
            StringBuilder response = new StringBuilder();

            while(scanner.hasNextLine()){
                response.append(scanner.nextLine());
            }
            responseStream.close();

            JsonNode jsonNode = objectMapper.readTree(response.toString());

            email = jsonNode.get(0).get("email").asText();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return email;
    }

    public String getTokenUrlGithub(){
        return  "https://github.com/login/oauth/access_token?"
                + "client_id=" + Constant.clientId
                + "&client_secret=" + Constant.clientSecret
                + "&code=";
    }

    public String getUserId(OAuth2User oauth2User) {
        try{
            String oauthUserId = oauth2User.getAttribute("id");
            return oauthUserId;

        } catch (Exception e){
            return e.getMessage();
        }
    }



}

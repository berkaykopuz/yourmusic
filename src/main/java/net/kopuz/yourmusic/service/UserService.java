package net.kopuz.yourmusic.service;

import net.kopuz.yourmusic.entity.User;
import net.kopuz.yourmusic.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveOrUpdateOauthUser(String username,String email,String oauthUserId, String oauthProvider){
        Optional<User> existUser = userRepository.findByOauthUserId(oauthUserId);

        if(existUser.isPresent()){
            return;
        }
        else{
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setOauthUserId(oauthUserId);
            user.setOauthProvider(oauthProvider);

            userRepository.save(user);
        }

    }

    public String getIdByOAuthId(String oauthUserId){

        try{
            String id = userRepository.findIdByOauthUserId(oauthUserId);
            return id;
        }
        catch(Exception e){
            throw e;
        }

    }




}

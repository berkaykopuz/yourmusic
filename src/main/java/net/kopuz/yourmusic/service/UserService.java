package net.kopuz.yourmusic.service;

import net.kopuz.yourmusic.entity.User;
import net.kopuz.yourmusic.exception.UserNotFoundException;
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

    public void saveOAuthUser(String username,String email,String oauthUserId, String oauthProvider){
        Optional<User> existUser = userRepository.findByOauthUserId(oauthUserId);

        if(existUser.isPresent()){
            return;
        }
        else{
            User user = new User(
                    username,
                    email,
                    oauthUserId,
                    oauthProvider
            );

            userRepository.save(user);
        }

    }

    public String getIdByOAuthId(String oauthUserId){
        String id = userRepository.findIdByOauthUserId(oauthUserId);

        if(id.isEmpty()){
            throw new UserNotFoundException("user with [%s] not found".formatted(oauthUserId));
        }

        return id;

    }




}

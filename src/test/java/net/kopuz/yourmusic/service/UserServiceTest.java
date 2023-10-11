package net.kopuz.yourmusic.service;

import net.kopuz.yourmusic.entity.User;
import net.kopuz.yourmusic.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
    }

    @Test
    void testSaveOAuthUser_shouldSaveUser_whenUserNotExist(){
        String username = "user";
        String email = "user@gmail.com";
        String oAuthUserId = "oAuthId";
        String oAuthProvider = "github";

        User expected = new User(username, email, oAuthUserId, oAuthProvider);

        Mockito.when(userRepository.findByOauthUserId(oAuthUserId)).thenReturn(Optional.of(expected));
        //Mockito.when(userRepository.save(expected)).thenReturn(Mockito.any());

        Optional<User> result = userRepository.findByOauthUserId(oAuthUserId);

        assertEquals(expected, result.get());

        Mockito.verify(userRepository).findByOauthUserId(oAuthUserId);
        //Mockito.verify(userRepository).save(expected);
    }

    @Test
    void testSaveOAuthUser_shouldDoNothing_whenUserAlreadyHasExist(){
        String username = "user";
        String email = "user@gmail.com";
        String oAuthUserId = "oAuthId";
        String oAuthProvider = "github";

        User exist = new User(username, email, oAuthUserId, oAuthProvider);

        Mockito.doNothing();
    }
}
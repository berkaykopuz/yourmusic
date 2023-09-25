package net.kopuz.yourmusic.repository;

import net.kopuz.yourmusic.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserById(String id);
    Optional<User> findByOauthUserId(String oauthUserId);

    String findIdByOauthUserId(String oauthUserId);

}

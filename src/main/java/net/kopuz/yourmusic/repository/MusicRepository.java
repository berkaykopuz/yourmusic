package net.kopuz.yourmusic.repository;

import net.kopuz.yourmusic.entity.Music;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicRepository extends MongoRepository<Music, String> {

}

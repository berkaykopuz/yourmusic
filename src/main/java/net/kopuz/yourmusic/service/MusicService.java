package net.kopuz.yourmusic.service;

import net.kopuz.yourmusic.config.S3Buckets;
import net.kopuz.yourmusic.dto.MusicDto;
import net.kopuz.yourmusic.entity.Music;
import net.kopuz.yourmusic.entity.User;
import net.kopuz.yourmusic.exception.FileNotFoundException;
import net.kopuz.yourmusic.exception.MusicNotFoundException;
import net.kopuz.yourmusic.repository.MusicRepository;
import net.kopuz.yourmusic.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MusicService {
    private static final Logger log = LoggerFactory.getLogger(MusicService.class);
    private final MusicRepository musicRepository;
    private final OAuthService oAuthService;
    private final S3Service s3Service;
    private final S3Buckets s3Buckets;

    public MusicService(MusicRepository musicRepository, OAuthService oAuthService, S3Service s3Service, S3Buckets s3Buckets) {
        this.musicRepository = musicRepository;
        this.oAuthService = oAuthService;
        this.s3Service = s3Service;
        this.s3Buckets = s3Buckets;
    }

    public List<MusicDto> getAllMusics(){
        List<Music> musicList = musicRepository.findAll();
        if(!musicList.isEmpty()){
            List<MusicDto> musicDtos = musicList.stream()
                    .map(music -> MusicDto.convert(music))
                    .collect(Collectors.toList());
            log.info("all musics have listed");
            return musicDtos;

        }
        else{
            log.warn("has not found any music");
            throw new MusicNotFoundException("has not found any music");
        }

    }

    public MusicDto getMusicById(String id) throws Exception {
        Optional<Music> music = musicRepository.findById(id);
        return music.map(musicObject -> {
            return MusicDto.convert(musicObject);
        }).orElseThrow(() -> new MusicNotFoundException("music with [%s] id has not found".formatted(id)));
    }

    public String getTitleById(String id) throws Exception {
        MusicDto musicDto = getMusicById(id);
        return musicDto.title();
    }

    public MusicDto saveMusic(String title, String artist, OAuth2User oAuth2User, MultipartFile file){

        String userId = oAuthService.getUserId(oAuth2User);
        String fileId = UUID.randomUUID().toString();

        Music music = new Music();
        music.setTitle(title);
        music.setArtist(artist);
        music.setUpdatedTime(LocalDateTime.now());
        music.setUserId(userId);
        music.setFileId(fileId);

        uploadMusicFile(userId, file, fileId);

        log.info("music has saved successfully");
        return MusicDto.convert(musicRepository.save(music));
    }

    public void uploadMusicFile(String userId, MultipartFile file, String fileId){
        try{
            log.info("music has uploaded to s3 bucket");
            s3Service.putS3Object(
                    s3Buckets.getBucket(),
                    "musics/%s/%s".formatted(userId, fileId),
                    file.getBytes()
            );
        } catch(IOException e){
            throw new RuntimeException("failed to upload music file", e);
        }
    }

    public byte[] getMusicFile(String id){
        Optional<Music> music = musicRepository.findById(id);
        if(music.isPresent()){
            byte[] musicFile = s3Service.getObject(
                    s3Buckets.getBucket(),
                    "musics/%s/%s".formatted(music.get().getUserId(), music.get().getFileId())
            );

            if(musicFile != null){
                log.info("music has returned to user");
                return musicFile;
            }
            else{
                log.warn("file has not found");
                throw new FileNotFoundException("file has not found");
            }

        }
        else{
            log.warn("music has not found");
            throw new MusicNotFoundException("music with [%s] id not found.".formatted(id));
        }
    }

    public MusicDto updateMusic(Music music, String id){
        Optional<Music> updatedMusic = musicRepository.findById(id);

        if(updatedMusic.isPresent()){
            updatedMusic.get().setTitle(music.getTitle());
            updatedMusic.get().setArtist(music.getArtist());
            updatedMusic.get().setUpdatedTime(LocalDateTime.now());

            log.info("music has updated successfully");
            return MusicDto.convert(musicRepository.save(updatedMusic.get()));
        }
        else{
            throw new MusicNotFoundException("music with [%s] id not found".formatted(id));
        }
    }

}

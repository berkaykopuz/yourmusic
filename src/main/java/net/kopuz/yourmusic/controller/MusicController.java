package net.kopuz.yourmusic.controller;

import net.kopuz.yourmusic.dto.MusicDto;
import net.kopuz.yourmusic.entity.Music;
import net.kopuz.yourmusic.service.MusicService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping( "/api")
public class MusicController {
    private final MusicService musicService;

    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping("/music")
    public ResponseEntity<List<MusicDto>> getAllMusics(){
        return new ResponseEntity<>(musicService.getAllMusics(), HttpStatus.OK);
    }

    @PostMapping("/music/upload")
    public ResponseEntity<MusicDto> saveMusic(@AuthenticationPrincipal OAuth2User oAuth2User,
                                              @RequestParam("title") String title,
                                              @RequestParam("artist") String artist,
                                              @RequestParam("file")MultipartFile file){
        return new ResponseEntity<>(musicService.saveMusic(title, artist, oAuth2User, file), HttpStatus.OK);
    }

    @GetMapping("/music/download/{id}")
    public ResponseEntity<byte[]> getMusic(@PathVariable("id") String musicId) throws Exception {
        try {

            String title = musicService.getTitleById(musicId);
            byte[] musicFile = musicService.getMusicFile(musicId);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentDispositionFormData("attachment", title + ".mp3");

            return new ResponseEntity<>(musicFile, httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

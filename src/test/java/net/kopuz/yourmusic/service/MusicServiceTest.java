package net.kopuz.yourmusic.service;

import net.kopuz.yourmusic.config.S3Buckets;
import net.kopuz.yourmusic.dto.MusicDto;
import net.kopuz.yourmusic.entity.Music;
import net.kopuz.yourmusic.exception.MusicNotFoundException;
import net.kopuz.yourmusic.repository.MusicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


class MusicServiceTest {
    private MusicService musicService;
    private MusicRepository musicRepository;
    private OAuthService oAuthService;
    private S3Service s3Service;
    private S3Buckets s3Buckets;
    private OAuth2User oAuth2User;
    private MultipartFile file;


    @BeforeEach
    void setUp() {
        musicRepository = Mockito.mock(MusicRepository.class);
        oAuthService = Mockito.mock(OAuthService.class);
        s3Service = Mockito.mock(S3Service.class);
        s3Buckets = Mockito.mock(S3Buckets.class);

        musicService = new MusicService(musicRepository, oAuthService, s3Service, s3Buckets);
    }

    @Test
    void testGetAllMusics_shouldReturnMusicListWithMusicDto_whenAtLeastOneMusicObjectExist(){
        Music music1 = new Music("1",
                "cuma",
                "cakal",
                LocalDateTime.now(),
                "1",
                "1"

        );
        Music music2 = new Music("2",
                "darkfantasy",
                "kanyewest",
                LocalDateTime.now(),
                "2",
                "2"

        );
        List<Music> expectedResult = Arrays.asList(music1, music2);
        Mockito.when(musicRepository.findAll()).thenReturn(expectedResult);

        List<Music> result = musicRepository.findAll();

        assertEquals(expectedResult, result);

        Mockito.verify(musicRepository).findAll();
    }

    @Test
    void testGetAllMusic_shouldThrowRuntimeException_whenAnyMusicObjectIsNotFound(){
        String message = "has not found any music";

        Mockito.when(musicRepository.findAll()).thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> musicService.getAllMusics())
                .isInstanceOf(MusicNotFoundException.class)
                .hasMessage(message);

        Mockito.verify(musicRepository).findAll();
    }

    @Test
    void testSaveMusic_shouldSaveandReturnMusicDto_whenAllParametersGiven(@TempDir Path fileDir) throws IOException {
        String title = "title";
        String artist = "artist";
        String userId = "user1";
        String fileId = "file1";

        Path file = fileDir.resolve("abc.txt");
        Files.write(file, "".getBytes());
        InputStream stream = new FileInputStream(file.toFile());
        MockMultipartFile multipartFile = new MockMultipartFile("file", file.toFile().getName(),
                String.valueOf(MediaType.MULTIPART_FORM_DATA), stream);

        Music savedMusic = new Music(Mockito.anyString(), title, artist, LocalDateTime.now(),userId, fileId);

        Mockito.when(oAuthService.getUserId(oAuth2User)).thenReturn(userId);
        Mockito.doNothing().when(s3Service).putS3Object(Mockito.anyString(), Mockito.anyString(), Mockito.any());
        Mockito.when(musicRepository.save(Mockito.any(Music.class))).thenReturn(savedMusic);

        MusicDto result = musicService.saveMusic(title, artist, oAuth2User, multipartFile);

        assertNotNull(result);
        assertEquals(MusicDto.convert(savedMusic), result);

        Mockito.verify(musicRepository).save(Mockito.any(Music.class));

    }



}
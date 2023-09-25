package net.kopuz.yourmusic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.kopuz.yourmusic.entity.Music;


import java.time.LocalDateTime;

public record MusicDto(
        String title,
        String artist,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
        LocalDateTime updatedTime,
        String userId,
        String fileId

) {
    public static MusicDto convert(Music from){
        return new MusicDto(from.getTitle(), from.getArtist(), from.getUpdatedTime(), from.getUserId(), from.getFileId());
    }

}

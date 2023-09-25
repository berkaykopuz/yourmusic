package net.kopuz.yourmusic.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "music")
public class Music {
    @Id
    private String id;
    private String title;
    private String artist;
    private LocalDateTime updatedTime;
    private String userId;
    private String fileId;

    public Music() {
    }

    public Music(String id, String title, String artist, LocalDateTime updatedTime, String userId, String fileId) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.updatedTime = updatedTime;
        this.userId = userId;
        this.fileId = fileId;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public String getUserId() {
        return userId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}

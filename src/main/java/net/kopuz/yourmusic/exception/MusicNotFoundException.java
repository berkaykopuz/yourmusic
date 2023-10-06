package net.kopuz.yourmusic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MusicNotFoundException extends RuntimeException{
    public MusicNotFoundException(String message) {
        super(message);
    }

}

package net.kopuz.yourmusic;

import net.kopuz.yourmusic.controller.MusicController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "net.kopuz.yourmusic.*")
public class YourmusicApplication {
	public static void main(String[] args) {
		SpringApplication.run(YourmusicApplication.class, args);
	}

}

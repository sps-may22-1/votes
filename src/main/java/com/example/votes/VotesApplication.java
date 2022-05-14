package com.example.votes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class VotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(VotesApplication.class, args);
	}

}

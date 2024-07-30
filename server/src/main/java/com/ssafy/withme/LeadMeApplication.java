package com.ssafy.withme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class LeadMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeadMeApplication.class, args);
	}

}
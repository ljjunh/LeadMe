package com.ssafy.withme.global.config.chat;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@RequiredArgsConstructor
public class MongoConfig {

    private final MongoProperties mongoProperties;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoProperties.getUri());
    }

    @Bean
    public MongoTemplate mongoTemplate() {
//        return new MongoTemplate(mongoClient(), mongoProperties.getDatabase());
        return new MongoTemplate(mongoClient(), "test");
    }
}

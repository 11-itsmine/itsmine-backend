package com.sparta.itsmine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableMongoRepositories
public class ItsmineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItsmineApplication.class, args);
    }
}

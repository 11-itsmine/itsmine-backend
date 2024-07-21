package com.sparta.itsmine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
//        (exclude =  SecurityAutoConfiguration.class ) // 시큐리티 제외
public class ItsmineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItsmineApplication.class, args);
    }
}

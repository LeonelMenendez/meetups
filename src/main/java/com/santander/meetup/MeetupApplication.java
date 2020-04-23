package com.santander.meetup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MeetupApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetupApplication.class, args);
    }

}

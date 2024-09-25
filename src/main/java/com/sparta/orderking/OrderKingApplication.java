package com.sparta.orderking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class OrderKingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderKingApplication.class, args);
    }

}

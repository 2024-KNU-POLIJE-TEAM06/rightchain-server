package com.example.rightchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RightchainApplication {

    public static void main(String[] args) {
        SpringApplication.run(RightchainApplication.class, args);
    }

}
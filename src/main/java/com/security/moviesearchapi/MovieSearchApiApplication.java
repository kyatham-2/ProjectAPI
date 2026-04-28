package com.security.moviesearchapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication (scanBasePackages = "com.security")
public class MovieSearchApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieSearchApiApplication.class, args);
    }

}

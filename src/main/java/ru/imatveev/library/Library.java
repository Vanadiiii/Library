package ru.imatveev.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class Library {
    public static void main(String[] args) {
        SpringApplication.run(Library.class, args);
    }
}

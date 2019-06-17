package ru.rmamedov.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * @author Rustam Mamedov
 */

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class StartApp {
    public static void main(String[] args) {
        SpringApplication.run(StartApp.class);
    }
}

package com.sb2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@EnableAsync
@SpringBootApplication
public class Sb2Application {

    public static void main(String[] args) {
        SpringApplication.run(Sb2Application.class, args);
        log.info("Application started");
    }

}

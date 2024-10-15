package com.brian.process;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ProcessControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcessControlApplication.class, args);
    }

}

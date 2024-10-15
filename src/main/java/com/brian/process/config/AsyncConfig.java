package com.brian.process.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author brian
 * @date 2024/10/3 14:57
 */
@Configuration
public class AsyncConfig {

    @Bean("emailExecutor")
    public Executor getEmailExecutor() {
        return new ThreadPoolExecutor(8, 16, 1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(20), new ThreadPoolExecutor.CallerRunsPolicy());
    }
}

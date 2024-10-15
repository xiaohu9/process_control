package com.brian.process.service.async;

/**
 * @author brian
 * @date 2024/10/3 16:40
 */
public interface EmailService {
    void sendEmail(String toUser, String... msg);
}

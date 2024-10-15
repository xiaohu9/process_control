package com.brian.process.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author brian
 * @date 2024/10/3 14:53
 */
@Configuration
@Data
public class AppConfig {

    // 发送邮箱验证码的邮箱
    @Value("${spring.mail.username}")
    private String username;
}

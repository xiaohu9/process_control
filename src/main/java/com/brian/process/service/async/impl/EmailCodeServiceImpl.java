package com.brian.process.service.async.impl;

import com.brian.process.config.AppConfig;
import com.brian.process.service.async.EmailService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author brian
 * @date 2024/10/3 16:41
 */
@Slf4j
@Service
public class EmailCodeServiceImpl implements EmailService {

    @Resource
    private JavaMailSender mailSender;

    @Autowired
    private AppConfig appConfig;

    // TODO: 导入 redis 存放验证码

    @Async("emailExecutor")
    @Override
    public void sendEmail(String toUser, String... msg) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            // 定制发送信息
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(appConfig.getUsername());
            // 邮件收件人 1或多
            helper.setTo(toUser);

            // 邮件发送格式
        } catch (MessagingException e) {
            Throwable t = e.getCause();
            log.error("邮件发送失败", t);
        }
    }
}


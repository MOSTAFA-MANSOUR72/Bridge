package com.market.bridge.service.MailService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailSender;
    @Override
    public void sendEmailAlert(String recipient, String subject, String messageBody) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(mailSender);
        mailMessage.setTo(recipient);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageBody);

        try {
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}

package ru.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService{

    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender emailSender;

    @Autowired
    public NotificationService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendMessage(String to, String subject, String messageText) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@spring-kafka-test.ru");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(messageText);
        emailSender.send(message);
        logger.info("Сообщение отправлено на почту успешно!");
    }
}

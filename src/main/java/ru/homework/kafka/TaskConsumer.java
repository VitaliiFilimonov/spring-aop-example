package ru.homework.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.homework.service.NotificationService;

@Component
public class TaskConsumer {

    private final Logger logger = LoggerFactory.getLogger(TaskConsumer.class);

    private final NotificationService notificationService;

    @Autowired
    public TaskConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics="${kafka.topic}")
    public void updateTaskStatus(String message) {
        logger.info("Обработка сообщения! {}", message);
        notificationService.sendMessage("vitalifilimonov@yandex.ru", "Test", "Success update status!");
    }
}

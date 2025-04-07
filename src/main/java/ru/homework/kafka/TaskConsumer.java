package ru.homework.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.homework.service.TaskService;

import java.io.IOException;
import java.util.Map;

@Component
public class TaskConsumer {

    private final Logger logger = LoggerFactory.getLogger(TaskConsumer.class);

    private final TaskService taskService;

    @Autowired
    public TaskConsumer(TaskService taskService) {
        this.taskService = taskService;
    }

    @KafkaListener(topics="${kafka.topic}")
    public void updateTaskStatus(String message) {
        logger.info("Обработка сообщения! {}", message);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map;
        try {
            map = mapper.readValue(message, Map.class);
            taskService.updateTaskById(Long.parseLong(map.get("id")), map.get("status"));
        } catch (IOException ex) {
            logger.error("Переданы некорректные данные во время обновления задачи!");
        }
    }
}

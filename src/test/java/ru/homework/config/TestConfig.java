package ru.homework.config;

import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import ru.homework.repository.TaskRepository;
import ru.homework.service.TaskService;
import ru.homework.utils.TaskMapper;

@Profile("unit-test")
@Configuration
public class TestConfig {

    @Bean
    public TaskRepository taskRepository() {
        return Mockito.mock(TaskRepository.class);
    }

    @Bean
    public TaskMapper mapper() {
        return Mappers.getMapper(TaskMapper.class);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return Mockito.mock(KafkaTemplate.class);
    }

    @Bean
    public TaskService taskService() {
        return new TaskService(taskRepository(), mapper(), "test_topic", kafkaTemplate());
    }
}

package ru.homework.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.homework.config.IntegrationTestConfig;
import ru.homework.dto.TaskDTO;
import ru.homework.exception.TaskException;
import ru.homework.repository.TaskRepository;
import ru.homework.service.TaskService;

@SpringBootTest
@ActiveProfiles("integration-test")
@Testcontainers
@ContextConfiguration(loader = IntegrationTestConfig.class)
class JpaIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @BeforeEach
    void beforeEach() {
        taskRepository.deleteAll();
    }

    @Test
    void testCreateTask() throws TaskException {
        TaskDTO taskDTO = getTaskDTO();
        long id = taskService.createNewTask(taskDTO);
        TaskDTO taskDTOAfterSave = taskService.getTaskById(id);

        Assertions.assertEquals(id, taskDTOAfterSave.getId());
    }

    @Test
    void testUpdateTask() throws TaskException {
        TaskDTO taskDTO = getTaskDTO();
        taskService.createNewTask(taskDTO);

        taskDTO.setStatus(TaskDTO.TaskStatus.APPROVED);
        taskService.updateTaskById(1L, taskDTO);

        TaskDTO taskDTOAfterSave = taskService.getTaskById(1L);

        Assertions.assertEquals(taskDTO.getStatus().name(), taskDTOAfterSave.getStatus().name());
    }

    private TaskDTO getTaskDTO() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setDescription("description");
        taskDTO.setTitle("title");
        taskDTO.setUserId(1L);

        return taskDTO;
    }
}

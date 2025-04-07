package ru.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import ru.homework.dto.TaskDTO;
import ru.homework.exception.TaskException;
import ru.homework.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final String topic;

    private final TaskService taskService;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public TaskController(TaskService taskService, @Value("${kafka.topic}") String topic, KafkaTemplate<String, String> kafkaTemplate) {
        this.taskService = taskService;
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public long createNewTask(@RequestBody TaskDTO taskDTO) throws TaskException {
        return taskService.createNewTask(taskDTO);
    }

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable long id) throws TaskException {
        return taskService.getTaskById(id);
    }

    @PutMapping
    public void updateTask(@RequestBody String data) {
        kafkaTemplate.send(topic, data);
    }

    @DeleteMapping("/{id}")
    public long removeTaskById(@PathVariable long id) {
        return taskService.removeTaskById(id);
    }

    @GetMapping
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }
}

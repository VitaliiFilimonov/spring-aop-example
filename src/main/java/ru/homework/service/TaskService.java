package ru.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.homework.annotation.LogTimeInterval;
import ru.homework.annotation.SaveTaskLog;
import ru.homework.dto.TaskDTO;
import ru.homework.exception.TaskException;
import ru.homework.model.Task;
import ru.homework.repository.TaskRepository;
import ru.homework.utils.TaskMapper;
import ru.homework.utils.Util;

import java.util.List;

@Service
public class TaskService {
    private final String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    private final TaskMapper mapper;

    @Autowired
    public TaskService(TaskRepository taskRepository,
                       TaskMapper mapper,
                       @Value("${kafka.topic}") String topic,
                       KafkaTemplate<String, String> kafkaTemplate) {
        this.taskRepository = taskRepository;
        this.mapper = mapper;
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @SaveTaskLog
    public long createNewTask(TaskDTO taskDTO) throws TaskException {
        if (!Util.isDTOEmpty(taskDTO)) {
            Task task = mapper.mapToTask(taskDTO);
            task.setStatus(Task.TaskStatus.OPEN);
            return taskRepository.save(task).getId();
        }

        throw new TaskException("Не переданы данные для сохранения!");
    }

    public TaskDTO getTaskById(long id) throws TaskException {
        return mapper.mapToTaskDTO(
                taskRepository.findById(id)
                        .orElseThrow(
                                () -> new TaskException("Not found task with id '" + id + "'!")
                        )
        );
    }

    @Transactional
    public void updateTaskById(long id, TaskDTO taskDTO) {
        taskRepository.findById(id).ifPresentOrElse(
                task -> {
                    if (taskDTO.getStatus() != null && !taskDTO.getStatus().name().equals(task.getStatus().name()))
                        kafkaTemplate.send(topic, taskDTO.toString());

                    mapper.update(taskDTO, task);
                    taskRepository.save(task);
                },
                () -> logger.warn("Task with id = {} not found", id)
        );
    }

    @LogTimeInterval
    public long removeTaskById(Long id) {
        taskRepository.deleteById(id);
        return id;
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(
                        mapper::mapToTaskDTO
                ).toList();
    }
}

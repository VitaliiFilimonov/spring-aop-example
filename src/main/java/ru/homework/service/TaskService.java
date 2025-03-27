package ru.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    private final TaskMapper mapper;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskMapper mapper) {
        this.taskRepository = taskRepository;
        this.mapper = mapper;
    }

    @SaveTaskLog
    public long createNewTask(TaskDTO taskDTO) throws TaskException {
        if (!Util.isDTOEmpty(taskDTO)) {
            Task task = mapper.mapToTask(taskDTO);
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
    public long updateTaskById(long id, TaskDTO taskDTO) {
        taskRepository.findById(id).ifPresentOrElse(
                task -> {
                    mapper.update(taskDTO, task);
                    taskRepository.save(task);
                },
                () -> logger.warn("Task with id = {} not found", id)
        );

        return id;
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

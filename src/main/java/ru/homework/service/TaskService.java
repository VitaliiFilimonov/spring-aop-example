package ru.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.homework.model.Task;
import ru.homework.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    public Long createNewTask(Task task) {
        return taskRepository.save(task).getId();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Transactional
    public Long updateTaskById(Long id, Task newTask) {
        taskRepository.findById(id).ifPresentOrElse(
                task -> {
                    task.setTitle(newTask.getTitle());
                    task.setUserId(newTask.getUserId());
                    taskRepository.save(task);
                },

                () -> logger.warn("Task with id = {} not found", id)
        );

        return id;
    }

    public Long removeTaskById(Long id) {
        taskRepository.deleteById(id);
        return id;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}

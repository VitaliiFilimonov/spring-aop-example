package ru.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.homework.annotation.LogTimeInterval;
import ru.homework.exception.TaskException;
import ru.homework.model.Task;
import ru.homework.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @LogTimeInterval
    @PostMapping
    public Long createNewTask(@RequestBody Task task) {
        return taskService.createNewTask(task);
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) throws TaskException {
        return taskService.getTaskById(id).orElseThrow(
                () -> new TaskException("Not found task with id '" + id + "'!")
        );
    }

    @PutMapping("/{id}")
    public Long updateTask(@PathVariable Long id, @RequestBody Task newTask) {
        return taskService.updateTaskById(id, newTask);
    }

    @DeleteMapping("/{id}")
    public Long removeTaskById(@PathVariable Long id) {
        return taskService.removeTaskById(id);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }
}

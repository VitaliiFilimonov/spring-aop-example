package ru.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.homework.dto.TaskDTO;
import ru.homework.exception.TaskException;
import ru.homework.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public long createNewTask(@RequestBody TaskDTO taskDTO) throws TaskException {
        return taskService.createNewTask(taskDTO);
    }

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable long id) throws TaskException {
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public void updateTask(@PathVariable long id, @RequestBody TaskDTO taskDTO) {
        taskService.updateTaskById(id, taskDTO);
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

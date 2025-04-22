package ru.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.homework.config.TestConfig;
import ru.homework.dto.TaskDTO;
import ru.homework.exception.TaskException;
import ru.homework.model.Task;
import ru.homework.repository.TaskRepository;
import ru.homework.utils.TaskMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("unit-mvc-test")
@WebMvcTest(TaskController.class)
@ContextConfiguration(classes = {TestConfig.class})
class TaskControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private TaskController taskController;

    @Autowired
    private TaskMapper mapper;

    @Autowired
    private TaskRepository taskRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(taskController)
                .build();
    }

    @Test
    void createNewTaskSuccess() throws Exception {
        TaskDTO taskDTO = getTaskDTO();
        Task task = mapper.mapToTask(taskDTO);

        when(taskRepository.save(any(Task.class))).thenReturn(task);
        mockMvc.perform(post("/tasks/")
                        .content(objectMapper.writeValueAsString(taskDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void createNewTaskThrowTaskException() {
        TaskDTO taskDTO = new TaskDTO();

        Assertions.assertThatThrownBy(() -> {
            mockMvc.perform(post("/tasks/")
                            .content(objectMapper.writeValueAsString(taskDTO))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());
        }).hasCause(new TaskException("Не переданы данные для сохранения!"));
    }

    @Test
    void getTaskByIdSuccess() throws Exception {
        TaskDTO taskDTO = getTaskDTO();
        Task task = mapper.mapToTask(taskDTO);

        when(taskRepository.findById(taskDTO.getId())).thenReturn(Optional.ofNullable(task));
        mockMvc.perform(get("/tasks/")
                        .param("id", taskDTO.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateTaskByIdSuccess() throws Exception {
        Task task = mapper.mapToTask(getTaskDTO());
        task.setStatus(Task.TaskStatus.OPEN);

        TaskDTO taskDTO = getTaskDTO();
        taskDTO.setStatus(TaskDTO.TaskStatus.REVIEW);

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        task = mapper.mapToTask(taskDTO);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        mockMvc.perform(put("/tasks/{id}", 1L)
                        .content(objectMapper.writeValueAsString(taskDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    private TaskDTO getTaskDTO() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setDescription("description");
        taskDTO.setTitle("title");
        taskDTO.setUserId(1L);

        return taskDTO;
    }

    private List<TaskDTO> getAllTasksDTO(TaskDTO taskDTO, int size) {
        List<TaskDTO> taskDTOList = new ArrayList<>();

        for (long i = 0; i < size; i++) {
            taskDTO.setId(i + 1);
            taskDTOList.add(taskDTO);
        }

        return taskDTOList;
    }
}
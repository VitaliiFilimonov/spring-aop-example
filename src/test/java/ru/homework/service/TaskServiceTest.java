package ru.homework.service;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.homework.config.TestConfig;
import ru.homework.dto.TaskDTO;
import ru.homework.exception.TaskException;
import ru.homework.model.Task;
import ru.homework.repository.TaskRepository;
import ru.homework.utils.TaskMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("unit-test")
@ContextConfiguration(classes = {TestConfig.class})
class TaskServiceTest {

    @Mock
    private Appender mockAppender;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper mapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private TaskService taskService;

    @Test
    void createNewTaskSuccess() throws TaskException {
        TaskDTO taskDTO = getTaskDTO();

        Task task = mapper.mapToTask(taskDTO);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        long id = taskService.createNewTask(taskDTO);

        assertEquals(taskDTO.getId(), id);
    }

    @Test
    void createNewTaskThrowTaskException() {
        TaskDTO taskDTO = new TaskDTO();
        assertThrows(TaskException.class,
                () -> taskService.createNewTask(taskDTO));
    }

    @Test
    void getTaskByIdSuccess() throws TaskException {
        TaskDTO taskDTO = getTaskDTO();
        Task task = mapper.mapToTask(taskDTO);

        when(taskRepository.findById(taskDTO.getId())).thenReturn(Optional.ofNullable(task));

        TaskDTO taskDTOAfterServiceCall = taskService.getTaskById(taskDTO.getId());

        assertEquals(taskDTO.getDescription(), taskDTOAfterServiceCall.getDescription());
    }

    @Test
    void getTaskByIdThrowTaskException() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TaskException.class,
                () -> taskService.getTaskById(anyLong()));
    }

    @Test
    void updateTaskByIdSuccess() {
        Task task = mapper.mapToTask(getTaskDTO());
        task.setStatus(Task.TaskStatus.OPEN);

        TaskDTO taskDTO = getTaskDTO();
        taskDTO.setStatus(TaskDTO.TaskStatus.REVIEW);

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        task = mapper.mapToTask(taskDTO);

        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        taskService.updateTaskById(task.getId(), taskDTO);

        assertEquals(taskDTO.getStatus().name(), task.getStatus().name());
    }

    @Test
    void updateTaskByIdWithLogging() {
        Logger taskServiceLogger = (Logger) LoggerFactory.getLogger(TaskService.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        taskServiceLogger.addAppender(listAppender);

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        taskService.updateTaskById(1L, new TaskDTO());

        List<ILoggingEvent> logsList = listAppender.list;

        assertEquals("Task with id = 1 not found", logsList.get(0).getFormattedMessage());
    }

    @Test
    void removeTaskById() {
        taskService.removeTaskById(anyLong());
        verify(taskRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void getAllTasks() {
        List<Task> taskList = getAllTasksDTO(getTaskDTO(), 5)
                .stream()
                .map(mapper::mapToTask)
                .toList();

        when(taskRepository.findAll()).thenReturn(taskList);

        taskService.getAllTasks();
        verify(taskRepository, times(1)).findAll();
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
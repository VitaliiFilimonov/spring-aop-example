package ru.homework.utils;

import org.mapstruct.*;
import ru.homework.dto.TaskDTO;
import ru.homework.model.Task;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {
    Task mapToTask(TaskDTO taskDTO);

    TaskDTO mapToTaskDTO(Task task);

    @Mappings({
            @Mapping(target = "title", source = "title"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "userId", source = "userId"),
            @Mapping(target = "status", source = "status")
    })
    void update(TaskDTO taskDTO, @MappingTarget Task task);
}

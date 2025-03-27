package ru.homework.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import ru.homework.dto.TaskDTO;
import ru.homework.model.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task mapToTask(TaskDTO taskDTO);

    TaskDTO mapToTaskDTO(Task task);

    @Mappings({
            @Mapping(target = "title", source = "title"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "userId", source = "userId")
    })
    void update(TaskDTO taskDTO, @MappingTarget Task task);
}

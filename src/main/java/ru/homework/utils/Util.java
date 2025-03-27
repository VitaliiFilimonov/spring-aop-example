package ru.homework.utils;

import org.springframework.util.ObjectUtils;
import ru.homework.dto.TaskDTO;

public class Util {

    private Util() {
        throw new RuntimeException("Util class!");
    }

    public static boolean isDTOEmpty(TaskDTO taskDTO) {
        if (!ObjectUtils.isEmpty(taskDTO)) {
            if (taskDTO.getDescription() != null && !taskDTO.getDescription().isEmpty())
                return false;

            return taskDTO.getTitle() == null || taskDTO.getTitle().isEmpty();
        }

        return true;
    }
}

package ru.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.homework.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}

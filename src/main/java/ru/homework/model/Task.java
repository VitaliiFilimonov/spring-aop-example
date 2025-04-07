package ru.homework.model;

import jakarta.persistence.*;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @SequenceGenerator(name = "task_seq", sequenceName = "task_seq", allocationSize = 1)
    @GeneratedValue(generator = "task_seq")
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "task_status")
    private TaskStatus status;

    @Column(name = "user_id")
    private Long userId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public enum TaskStatus {
        OPEN, REVIEW, APPROVED, REJECTED;
    }
}

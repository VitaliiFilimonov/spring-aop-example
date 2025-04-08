package ru.homework.dto;

public class UpdateStatusDTO {

    private Long id;

    private TaskStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UpdateStatusDTO{" +
                "id=" + id +
                ", status=" + status +
                '}';
    }

    public enum TaskStatus {
        OPEN, REVIEW, APPROVED, REJECTED;
    }
}

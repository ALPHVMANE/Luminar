package model;

import java.time.LocalDateTime;
import java.util.*;

public class NonRecurrentTask extends Task {
    private Calendar dueDate;

    public NonRecurrentTask(String id, String title, String description, Category category, Status status, String userId, Priority priority, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(id, title, description, category, status, userId, priority, updatedAt, createdAt);
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

}

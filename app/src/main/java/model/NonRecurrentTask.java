package model;

import java.time.LocalDateTime;
import java.util.*;

public class NonRecurrentTask extends Task {
    private Calendar dueDate;

    public NonRecurrentTask(String id, String title, String description, Category category, Status status, String userId, Priority priority, boolean enableNotif, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(id, title, description, category, status, userId, priority, enableNotif, updatedAt, createdAt);
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

}

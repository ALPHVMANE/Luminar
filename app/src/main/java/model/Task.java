package model;

import java.io.Serializable;
import java.time.*;

public abstract class Task implements Serializable {
    private String id;
    private String title;
    private String description;
    private Category category;
    private Status status;
    private String userId;
    private long updatedAt;
    private long createdAt;
    private Priority priority;
    private boolean enableNotif;

    public Task(String id, String title, String description, Category category, Status status, String userId, Priority priority, boolean enableNotif, long updatedAt, long createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.status = status;
        this.userId = userId;
        this.priority = priority;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public Task() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Status getStatus() {
        return status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String user) {
        this.userId = user;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public boolean isEnableNotif() {
        return enableNotif;
    }

    public void setEnableNotif(boolean enableNotif) {
        this.enableNotif = enableNotif;
    }
    //method
    public abstract void delete(String taskId);
}

package model;

import java.util.*;

public class UserTasks {
    private ArrayList<RecurrentTask> recTask;
    private ArrayList<NonRecurrentTask> singTask;

    public UserTasks(ArrayList<RecurrentTask> recTask, ArrayList<NonRecurrentTask> singTask) {
        this.recTask = new ArrayList<>();
        this.singTask = new ArrayList<>();
    }

    //CRUD for BOTH Lists
    public void addRecTask(RecurrentTask task){
        this.recTask.add(task);
    }
    public void addSingTask(NonRecurrentTask task){
        this.singTask.add(task);
    }

    public void rmRecTask(String taskId){
        this.recTask.removeIf(task -> task.getId().equals(taskId));
    }
    public void rmSingTask(String taskId){
        this.singTask.removeIf(task -> task.getId().equals(taskId));
    }

}

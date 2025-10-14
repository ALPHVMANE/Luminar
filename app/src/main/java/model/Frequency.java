package model;

import java.util.*;

public class Frequency {
    private ArrayList<Task> tasks;

    public Frequency(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
}

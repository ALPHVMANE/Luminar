package model;

import java.time.*;

public class Task {
    private int id;
    private LocalDate startDate; //yyyy-MM-dd
    private int startTime; //hour
    private String name;
    private String description;
    private Frequency frequency;
    private Category category;


    public Task(int id, LocalDate startDate, String name, Frequency frequency) {
        this.id = id;
        this.startDate = startDate;
        this.name = name;
        this.frequency = frequency;
    }
    public Task(int id, LocalDate startDate, String name, String description, Frequency frequency, Category category) {
        this.id = id;
        this.startDate = startDate;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.category = category;
    }


    public Task(int id, LocalDate startDate, int startTime, String name, String description, Frequency frequency, Category category) {
        this.id = id;
        this.startDate = startDate;
        this.startTime = startTime;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.category = category;
    }
}

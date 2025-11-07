package model;

import java.util.Calendar;
import java.util.Calendar;

import java.time.*;
import java.time.LocalTime;

public class RecurrentTask extends Task{
    Frequency freq;
    Calendar startCalendar;
    Calendar endCalendar;
    Calendar nextOccurence;

    public RecurrentTask(String id, String title, String description, Category category, Status status, String userId, Priority priority, boolean enableNotif, LocalDateTime updatedAt, LocalDateTime createdAt) {
        super(id, title, description, category, status, userId, priority, enableNotif, updatedAt, createdAt);
    }


    public Frequency getFreq() {
        return freq;
    }

    public void setFreq(Frequency freq) {
        this.freq = freq;
    }

    public Calendar getStartCalendar() {
        return startCalendar;
    }

    public void setStartCalendar(Calendar startCalendar) {
        this.startCalendar = startCalendar;
    }

    public Calendar getEndCalendar() {
        return endCalendar;
    }

    public void setEndCalendar(Calendar endCalendar) {
        this.endCalendar = endCalendar;
    }

    public Calendar getNextOccurence() {
        return nextOccurence;
    }

    public void setNextOccurence(Calendar nextOccurence) {
        this.nextOccurence = nextOccurence;
    }
    
    //FUNCTIONS
    
    public Calendar calculateNextOccurence(){
        Calendar nextOccurence = this.nextOccurence;
        Frequency freq = this.freq;
        
        if (freq == Frequency.DAILY){
            nextOccurence.add(Calendar.DATE,1);
        }
        if (freq == Frequency.WEEKLY){
            nextOccurence.add(Calendar.DATE,7);
        }
        if (freq == Frequency.MONTHLY){
            nextOccurence.add(Calendar.MONTH,1);
        }
        if (freq == Frequency.YEARLY){
            nextOccurence.add(Calendar.YEAR,1);
        }
        return nextOccurence;
    }
}

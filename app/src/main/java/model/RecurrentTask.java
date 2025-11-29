package model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.time.*;
import java.util.function.Consumer;

public class RecurrentTask extends Task{
    Frequency freq;
    Calendar startCalendar;
    Calendar endCalendar;
    Calendar nextOccurence;

    public RecurrentTask(String id, String title, String description, Category category, Status status, String userId, Priority priority, boolean enableNotif, LocalDateTime updatedAt, LocalDateTime createdAt, Frequency freq, Calendar startDate, Calendar endDate, Calendar nextOccurence) {
        super(id, title, description, category, status, userId, priority, enableNotif, updatedAt, createdAt);
        this.freq = freq;
        this.startCalendar = startDate;
        this.endCalendar = endDate;
        this.nextOccurence = nextOccurence;
    }


    public Frequency getFreq() {return freq;}

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
    
    //METHODS
    
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

    //delete
    @Override
    public void delete(String id) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("recurringTasks").child(Global.getUid()).child(id);
        ref.removeValue()
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Recurrent Task Successfully deleted"))
                .addOnFailureListener(e -> Log.e("Firebase", "Unable to delete recurrent task :" + e.getMessage()));
    }
    //save
    public void save(RecurrentTask task){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("recurringTasks");
        ref.child(Global.getUid()).child(task.getId()).setValue(task)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Recurrent task was successfully saved to the database"))
                .addOnFailureListener(e -> Log.e("Firebase", "Unable to save recurrent task : " + e.getMessage()));
    }

    //load
    public void load(String id, Consumer<RecurrentTask> onResult){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("recurringTasks").child(Global.getUid()).child(id);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RecurrentTask task = snapshot.getValue(RecurrentTask.class);
                onResult.accept(task);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onResult.accept(null);
            }
        });
    }
}

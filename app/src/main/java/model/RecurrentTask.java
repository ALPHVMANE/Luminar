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
    long startCalendar;
    long endCalendar;
    long nextOccurence;

    public RecurrentTask(String id, String title, String description, Category category, Status status, String userId, Priority priority, boolean enableNotif, long updatedAt,
                         long createdAt, Frequency freq, long startDate, long endDate, long nextOccurence) {
        super(id, title, description, category, status, userId, priority, enableNotif, updatedAt, createdAt);
        this.freq = freq;
        this.startCalendar = startDate;
        this.endCalendar = endDate;
        this.nextOccurence = nextOccurence;
    }

    public RecurrentTask() {
        super();
    }


    public Frequency getFreq() {return freq;}

    public void setFreq(Frequency freq) {
        this.freq = freq;
    }

    public long getStartCalendar() {
        return startCalendar;
    }

    public void setStartCalendar(long startCalendar) {
        this.startCalendar = startCalendar;
    }

    public long getEndCalendar() {
        return endCalendar;
    }

    public void setEndCalendar(long endCalendar) {
        this.endCalendar = endCalendar;
    }

    public long getNextOccurence() {
        return nextOccurence;
    }

    public void setNextOccurence(long nextOccurence) {
        this.nextOccurence = nextOccurence;
    }
    
    //METHODS
    
    public long calculateNextOccurence(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.nextOccurence);
        
        if (freq == Frequency.DAILY){
            calendar.add(Calendar.DATE,1);
        }
        if (freq == Frequency.WEEKLY){
            calendar.add(Calendar.DATE,7);
        }
        if (freq == Frequency.MONTHLY){
            calendar.add(Calendar.MONTH,1);
        }
        if (freq == Frequency.YEARLY){
            calendar.add(Calendar.YEAR,1);
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
    public static void load(String id, Consumer<RecurrentTask> onResult){
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

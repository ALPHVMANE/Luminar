package model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

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

    //methods

    //delete

    public void delete(String id) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tasks").child(Global.getUid()).child(id);
        ref.removeValue()
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Non Recurrent Task Successfully deleted"))
                .addOnFailureListener(e -> Log.e("Firebase", "Unable to delete non recurrent task :" + e.getMessage()));
    }

    public void save(NonRecurrentTask task){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tasks");
        ref.child(Global.getUid()).child(task.getId()).setValue(task)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Non recurrent task was successfully saved to the database"))
                .addOnFailureListener(e -> Log.e("Firebase", "Unable to save non recurrent task : " + e.getMessage()));
    }

    public void load(String id, Consumer<NonRecurrentTask> onResult){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tasks").child(Global.getUid()).child(id);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NonRecurrentTask task = snapshot.getValue(NonRecurrentTask.class);
                onResult.accept(task);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onResult.accept(null);
            }
        });
    }
}
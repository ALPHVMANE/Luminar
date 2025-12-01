package model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.function.Consumer;

public class Notification {
    private String id;
    private String title;
    private String message;
    private String UserID;
    private String TaskID;
    private boolean newNotification;
    private Long Date;

    public String getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getTaskID() {
        return TaskID;
    }

    public void setTaskID(String taskID) {
        TaskID = taskID;
    }

    public boolean isNewNotification() {
        return newNotification;
    }

    public void setNewNotification(boolean newNotification) {
        this.newNotification = newNotification;
    }

    public Long getDate() {
        return Date;
    }

    public void setDate(Long date) {
        Date = date;
    }


    public Notification(boolean newNotification, String taskID, String userID, String message, String title, Long date) {
        this.newNotification = newNotification;
        TaskID = taskID;
        UserID = userID;
        this.message = message;
        this.title = title;
        this.Date = date;
    }

    public Notification() {

    }

    //delete
    public void delete(String notificationID) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("notifications").child(Global.getUid()).child(notificationID);
        ref.removeValue()
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Notification Successfully deleted"))
                .addOnFailureListener(e -> Log.e("Firebase", "Unable to delete notification :" + e.getMessage()));
    }

    //save
    public void save(Notification notification){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("notifications");
        ref.child(Global.getUid()).child(notification.getId()).setValue(notification)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Notification was successfully saved to the database"))
                .addOnFailureListener(e -> Log.e("Firebase", "Unable to save notification : " + e.getMessage()));
    }

    //load
    public static void load(String notificationID, Consumer<Notification> onResult){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("notifications").child(Global.getUid()).child(notificationID);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Notification notification = snapshot.getValue(Notification.class);
                onResult.accept(notification);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onResult.accept(null);
            }
        });
    }
    public String getFormattedDate(){
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(Date));
    }
}

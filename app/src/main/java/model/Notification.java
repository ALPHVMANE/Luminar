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

    // ADD THIS SETTER
    public void setId(String id) {
        this.id = id;
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

    // Constructor with ID
    public Notification(String id, boolean newNotification, String taskID, String userID, String message, String title, Long date) {
        this.id = id;
        this.newNotification = newNotification;
        TaskID = taskID;
        UserID = userID;
        this.message = message;
        this.title = title;
        this.Date = date;
    }

    // Constructor without ID (for creating new notifications)
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

    //save - UPDATED to handle null ID
    public void save(Notification notification){
        Log.d("notification", "=== SAVE METHOD CALLED ===");
        Log.d("notification", "Notification ID: " + notification.getId());
        Log.d("notification", "User ID: " + Global.getUid());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("notifications");

        // If ID is null, generate one
        if (notification.getId() == null || notification.getId().isEmpty()) {
            String newId = ref.child(Global.getUid()).push().getKey();
            notification.setId(newId);
            Log.d("notification", "Generated new ID: " + newId);
        }

        String userId = Global.getUid();
        String notifId = notification.getId();

        Log.d("Notification", "Saving to path: notifications/" + userId + "/" + notifId);

        ref.child(userId).child(notifId).setValue(notification)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "✓ Notification was successfully saved to the database with ID: " + notification.getId());
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "✗ Unable to save notification : " + e.getMessage());
                    e.printStackTrace();
                });
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
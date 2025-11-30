package services;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.luminar.R;

public class TaskReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String taskTitle = intent.getStringExtra("taskTitle");
        String taskDescription = intent.getStringExtra("taskDescription");
        int notificationId = intent.getIntExtra("notificationId", 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "FCM_CHANNEL")
                .setContentTitle("Task Reminder: " + taskTitle)
                .setContentText(taskDescription != null && !taskDescription.isEmpty()
                        ? taskDescription
                        : "You have a task due!")
                .setSmallIcon(R.drawable.logo_luminar)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(notificationId, builder.build());
            Log.d("TaskReminderReceiver", "Notification posted successfully");
        }
        else {
            Log.e("TaskReminderReceiver", "NotificationManager is null");
        }
    }
}

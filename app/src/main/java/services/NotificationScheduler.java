package services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;


public class NotificationScheduler {
    private static final String TAG = "NotificationScheduler";

    /**
     * Schedule a notification for a task
     * @param context Application context
     * @param taskId Unique task ID
     * @param taskTitle Task title
     * @param taskDescription Task description
     * @param triggerTimeMillis When to show notification (in milliseconds since epoch)
     */

    public static void scheduleTaskReminder(Context context, String taskId, String taskTitle, String taskDescription, long triggerTimeMillis){
        Log.d(TAG, "Scheduling notification for task: " + taskId);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TaskReminderReceiver.class);
        intent.putExtra("taskTitle", taskTitle);
        intent.putExtra("taskDescription", taskDescription);
        intent.putExtra("notificationId", taskId.hashCode());

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                taskId.hashCode(),
                intent,
                flags
        );
        if(alarmManager != null){
            if (triggerTimeMillis > System.currentTimeMillis()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            triggerTimeMillis,
                            pendingIntent
                    );
                }else {
                    alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            triggerTimeMillis,
                            pendingIntent
                    );
                }
                Log.d(TAG, " Notification scheduled successfully");
            }else{
                Log.w(TAG, "Trigger time is in the past, notification not scheduled");
            }
        }else {
            Log.e(TAG, "AlarmManager is null");
        }
    }

    /**
     * Cancel a scheduled notification
     * @param context Application context
     * @param taskId Unique task ID
     */
    public static void cancelTaskReminder(Context context, String taskId) {
        Log.d(TAG, "=== CANCELING TASK REMINDER ===");
        Log.d(TAG, "Task ID: " + taskId);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, TaskReminderReceiver.class);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                taskId.hashCode(),
                intent,
                flags
        );

        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            Log.d(TAG, "✓ Notification cancelled successfully");
        }
    }
}

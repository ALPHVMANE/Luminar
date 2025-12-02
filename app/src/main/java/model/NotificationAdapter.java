package model;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luminar.R;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Notification> notificationList;
    private static final String TAG = "NotificationAdapter";

    public NotificationAdapter(Context context, ArrayList<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @Override
    public int getCount() {
        return notificationList.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View oneItem, statusCircle;
        TextView tvTitle, tvMessage, tvTime;

        LayoutInflater inflater = LayoutInflater.from(context);
        oneItem = inflater.inflate(R.layout.notification_item, parent, false);

        statusCircle = oneItem.findViewById(R.id.statusCircle);
        tvTitle = oneItem.findViewById(R.id.tvNotificationTitle);
        tvMessage = oneItem.findViewById(R.id.tvNotificationMessage);
        tvTime = oneItem.findViewById(R.id.tvNotificationTime);

        Notification notification = (Notification) getItem(position);

        tvTitle.setText(notification.getTitle());
        tvMessage.setText(notification.getMessage());
        tvTime.setText(notification.getFormattedDate());

        // Change circle color depending on read/unread
        if (notification.isNewNotification()) {
            statusCircle.getBackground().setColorFilter(
                    0xFF2196F3, // blue
                    PorterDuff.Mode.SRC_IN
            );
        } else {
            statusCircle.getBackground().setColorFilter(
                    0xFFBDBDBD, // gray
                    PorterDuff.Mode.SRC_IN
            );
        }

        // Set up swipe-to-delete
        oneItem.setTag(position);
        SwipeToDeleteHelper swipeHelper = new SwipeToDeleteHelper(pos -> {
            deleteNotification(pos);
        });
        oneItem.setOnTouchListener(swipeHelper);

        // Add click listener to the entire notification item
        oneItem.setOnClickListener(v -> {
            String taskId = notification.getTaskID();

            if (taskId != null && !taskId.isEmpty()) {
                // Mark notification as read
                notification.setNewNotification(false);
                notification.save(notification);

                // Update the UI immediately
                statusCircle.getBackground().setColorFilter(
                        0xFFBDBDBD, // gray
                        PorterDuff.Mode.SRC_IN
                );

                // Navigate to CalendarActivity and then open the task bottom sheet
                navigateToCalendarAndOpenTask(taskId);
            }
        });

        return oneItem;
    }

    /**
     * Delete notification from list and database
     */
    private void deleteNotification(int position) {
        if (position >= 0 && position < notificationList.size()) {
            Notification notification = notificationList.get(position);

            // Delete from Firebase
            notification.delete(notification.getId());

            // Remove from list
            notificationList.remove(position);

            // Update UI
            notifyDataSetChanged();

            Toast.makeText(context, "Notification deleted", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Deleted notification at position: " + position);
        }
    }

    /**
     * Navigate to CalendarActivity and open the task bottom sheet
     */
    private void navigateToCalendarAndOpenTask(String taskId) {
        checkTaskTypeAndNavigate(taskId);
    }

    /**
     * Check task type and navigate to calendar with task info
     */
    private void checkTaskTypeAndNavigate(String taskId) {
        com.google.firebase.database.DatabaseReference recurringRef =
                com.google.firebase.database.FirebaseDatabase.getInstance()
                        .getReference("recurringTasks")
                        .child(Global.getUid())
                        .child(taskId);

        recurringRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    navigateToCalendar(taskId, true);
                } else {
                    checkNonRecurringTaskAndNavigate(taskId);
                }
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {
                checkNonRecurringTaskAndNavigate(taskId);
            }
        });
    }

    /**
     * Check if the task exists in non-recurring tasks and navigate
     */
    private void checkNonRecurringTaskAndNavigate(String taskId) {
        com.google.firebase.database.DatabaseReference nonRecurringRef =
                com.google.firebase.database.FirebaseDatabase.getInstance()
                        .getReference("tasks")
                        .child(Global.getUid())
                        .child(taskId);

        nonRecurringRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    navigateToCalendar(taskId, false);
                }
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {
                // Task not found
            }
        });
    }

    /**
     * Navigate to CalendarActivity with task information
     */
    private void navigateToCalendar(String taskId, boolean isRecurring) {
        android.content.Intent intent = new android.content.Intent(context, com.example.luminar.CalendarActivity.class);
        intent.putExtra("taskId", taskId);
        intent.putExtra("isRecurring", isRecurring);
        intent.putExtra("openBottomSheet", true);
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP | android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
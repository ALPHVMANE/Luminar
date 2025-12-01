package com.example.luminar;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import model.Global;
import model.Notification;
import model.NotificationAdapter;
import services.NavigationHelper;

public class NotificationActivity extends AppCompatActivity {
    private ArrayList<Notification> notifications;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initialize();
    }

    private void initialize() {
        ListView listNotifications = findViewById(R.id.listNotifications);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_notifications);
        NavigationHelper.setupBottomNavigation(this, bottomNav, R.id.nav_notifications);

        // Initialize the ArrayList before creating the adapter
        notifications = new ArrayList<>();
        adapter = new NotificationAdapter(this, notifications);
        listNotifications.setAdapter(adapter);
        loadNotifications();
    }

    private void loadNotifications() {
        DatabaseReference userNotificationsRef = FirebaseDatabase.getInstance().getReference().child("notifications").child(Global.getUid());
        userNotificationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clear notifications on load
                notifications.clear();
                if(!snapshot.exists()){
                    adapter.notifyDataSetChanged();
                    return;
                }
                //Values to check if all notifications are loaded
                int total = (int) snapshot.getChildrenCount();
                AtomicInteger loaded = new AtomicInteger(0);
                // loop to retrieve all info
                for (DataSnapshot child : snapshot.getChildren()){
                    String notificationId = child.getKey();
                    Notification.load(notificationId, notification -> {
                        //increment loaded counter
                        loaded.getAndIncrement();
                        //add notification to list
                        if (notification != null) {
                            notifications.add(notification);
                            adapter.notifyDataSetChanged();
                        }
                        //check if all notifications are loaded
                        if (loaded.get() == total) {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Database error: " + error.getMessage());
                adapter.notifyDataSetChanged();
            }
        });
    }
}
package services;

import android.app.Activity;
import android.content.Intent;

import com.example.luminar.AddTaskActivity;
import com.example.luminar.CalendarActivity;
import com.example.luminar.MainActivity;
import com.example.luminar.NotificationActivity;
import com.example.luminar.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationHelper {

    public static void setupBottomNavigation(Activity activity, BottomNavigationView bottomNav, int currentItemId) {

        bottomNav.setSelectedItemId(currentItemId);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            Intent intent = null;

            if (id == currentItemId) return true; // already on this tab

            if (id == R.id.nav_calendar) {
                intent = new Intent(activity, CalendarActivity.class);
            } else if (id == R.id.nav_add_task) {
                intent = new Intent(activity, AddTaskActivity.class);
            } else if (id == R.id.nav_notifications) {
                intent = new Intent(activity, NotificationActivity.class);
            }

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }

            return true;
        });
    }
}


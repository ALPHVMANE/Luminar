package services;

import android.app.Activity;
import android.content.Intent;

import com.example.luminar.AddTaskActivity;
import com.example.luminar.MainActivity;
import com.example.luminar.NotificationActivity;
import com.example.luminar.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationHelper {
    public static void setupBottomNavigation(Activity activity, BottomNavigationView bottomNavigationView, int currentMenuItemId) {
        // Set the current item as selected
        bottomNavigationView.setSelectedItemId(currentMenuItemId);

        // Set up navigation listener
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Don't navigate if already on the current screen
            if (itemId == currentMenuItemId) {
                return true;
            }

            Intent intent = null;

            if (itemId == R.id.nav_add_task) {
                intent = new Intent(activity, AddTaskActivity.class);
            } else
              if (itemId == R.id.nav_calendar) {
                intent = new Intent(activity, MainActivity.class);
            } else if (itemId == R.id.nav_notifications) {
                intent = new Intent(activity, NotificationActivity.class);
            }

            if (intent != null) {
                // Add flags to prevent activity stack buildup
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }

            return true;
        });
    }
}

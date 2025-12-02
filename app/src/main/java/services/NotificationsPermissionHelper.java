package services;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class NotificationsPermissionHelper {
    private static final String TAG = "NotificationPermission";
    public static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1001;

    /**
     * Check if notification permission is granted
     */
    public static boolean hasNotificationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED;
        }
        return true; // Permission not needed for Android 12 and below
    }

    /**
     * Request notification permission (Android 13+)
     */
    public static void requestNotificationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasNotificationPermission(activity)) {
                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE
                );
                Log.d(TAG, "Requesting POST_NOTIFICATIONS permission");
            } else {
                Log.d(TAG, "POST_NOTIFICATIONS permission already granted");
            }
        }
    }

    /**
     * Handle permission result
     * Call this from your Activity's onRequestPermissionsResult method
     */
    public static boolean handlePermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "POST_NOTIFICATIONS permission granted");
                return true;
            } else {
                Log.w(TAG, "POST_NOTIFICATIONS permission denied");
                return false;
            }
        }
        return false;
    }
}

package model;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.luminar.R;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Notification> notificationList;

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

        return oneItem;
    }
}

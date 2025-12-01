package model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luminar.R;

import java.util.ArrayList;

public class TaskAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Task> taskList;
    private final LayoutInflater inflater;

    public TaskAdapter(Context context, ArrayList<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
        this.inflater = LayoutInflater.from(context);
    }

    // ViewHolder Pattern (important)
    private static class ViewHolder {
        TextView tvTitle;
        TextView tvStatus;
        TextView tvCategory;
        ImageView imColor;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Task getItem(int position) {   // Return Task instead of Object
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        // Reuse convertView if possible
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.task_item, parent, false);

            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tvTitle);
            holder.tvStatus = convertView.findViewById(R.id.tvStatus);
            holder.tvCategory = convertView.findViewById(R.id.tvCategory);
            holder.imColor = convertView.findViewById(R.id.colorCategory);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Task task = getItem(position);

        // Populate data
        holder.tvTitle.setText(task.getTitle());
        holder.tvStatus.setText(task.getStatus().name());
        holder.tvCategory.setText(task.getCategory().getName());

        // Set category color bubble background
        String colorString = task.getCategory().getHex();  // Assuming "FF0000" (without #)

        GradientDrawable bg = (GradientDrawable) holder.imColor.getBackground();
        bg.setColor(Color.parseColor(colorString));

        return convertView;
    }
}

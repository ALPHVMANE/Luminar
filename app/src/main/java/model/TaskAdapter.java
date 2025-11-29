package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luminar.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class TaskAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Task> taskList;

    Task task;

    public TaskAdapter(Context context, ArrayList<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    // Implement the methods of BaseAdapter class

    // getCount : total number of items that should be displayed in the listview
    @Override
    public int getCount() {
        return taskList.size();
    }

    // getItem : the item that should be in position : position in the listview
    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    // getItemId : get item index in the position : position
    @Override
    public long getItemId(int position) {
        return position;
    }

    // getView : the view that should be displayed in the position : position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1 - Declaration
        // In this method you need to display the task (photo, full name, team name)
        // We return a view that represent the task

        View oneItem;
        ImageView imPhoto, imMore;
        TextView tvFullName, tvTeamName;

        // These widgets are stored/located in one_item.xml
        // ==> We need to do an operation that consist to convert this xml file (one_item.xml) to a java view object
        // This conversion is called : Layout inflation

        // 2 - Layout inflation
        LayoutInflater inflater = LayoutInflater.from(context);
        oneItem = inflater.inflate(R.layout.task_item,parent,false);

        // 3 - Reference each widget in view (one_item.xml)
        tvTitle = oneItem.findViewById(R.id.tvTitle);
        tvStat = oneItem.findViewById(R.id.tvStatus);
        tvCat = oneItem.findViewById(R.id.tvCategory);

        imMore = oneItem.findViewById(R.id.imMore);
        imMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = (task)getItem(position);
                Snackbar.make(v,"Year of birth is "+task.getYearOfBirth(),
                        Snackbar.LENGTH_LONG).show();
            }
        });

        // 4 - Manipulate (Populate) the widgets
        task = (task)getItem(position);
        tvFullName.setText(task.getFullName());
        tvTeamName.setText(task.getTeamName());
        String photoStr = task.getPhoto();
        int photoResId = context
                .getResources()
                .getIdentifier("drawable/"+photoStr,null,context.getPackageName());

        imPhoto.setImageResource(photoResId);

        // 5 - Return the view oneItem
        return oneItem;
    }
}

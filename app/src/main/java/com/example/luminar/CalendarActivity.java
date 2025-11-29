package com.example.luminar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import model.*;

public class CalendarActivity extends AppCompatActivity implements AdapterView.OnItemClickListener  {
    CalendarView calendar;
    TextView calendarDate;
    ListView lvTasks;

    ImageView colorCategory;
    ArrayList<Task> taskList;
    TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initialize();
    }
    private void initialize() {
        calendar = findViewById(R.id.calendarView);
        calendarDate = findViewById(R.id.calendarDate);
        lvTasks = findViewById(R.id.listTasks);
        lvTasks.setOnItemClickListener(this);

        // Initialize task list
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, taskList);
        lvTasks.setAdapter(taskAdapter);

        // Example: add some dummy tasks

        taskAdapter.notifyDataSetChanged();

        // Listen to calendar changes
        calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (month + 1) + "/" + year;
            calendarDate.setText(date);

            // Optional: filter tasks for this date
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
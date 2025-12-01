package com.example.luminar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import model.*;
import services.NavigationHelper;

public class CalendarActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, CalendarView.OnDateChangeListener, ValueEventListener {
    CalendarView calendar;
    TextView calendarDate;
    ListView lvTasks;
    HashMap<String, String> taskIds;
    ArrayList<Task> currentDateTasks;
    TaskAdapter taskAdapter;
    private boolean isRecurring = false;
    DatabaseReference nTasksDB = FirebaseDatabase.getInstance().getReference("tasks");
    DatabaseReference rTasksDB = FirebaseDatabase.getInstance().getReference("recurringTasks");

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

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_calendar);
        NavigationHelper.setupBottomNavigation(this, bottomNav, R.id.nav_calendar);

        taskIds = new HashMap<>();
        nTasksDB.child(Global.getUid()).addValueEventListener(this);
        isRecurring = true;
        rTasksDB.child(Global.getUid()).addValueEventListener(this);

        currentDateTasks = new ArrayList<>();
        calendar.setOnDateChangeListener(this);

        taskAdapter = new TaskAdapter(this, currentDateTasks);
        lvTasks.setAdapter(taskAdapter);
        taskAdapter.notifyDataSetChanged();
    }

    //to learn more: https://stackoverflow.com/questions/32867968/how-to-get-item-from-listview-onitemclick-in-android
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        Task clickedTask = (Task) parent.getItemAtPosition(position);
        Intent intent = new Intent(this, BottomSheetActivity.class);
        if (clickedTask instanceof NonRecurrentTask) {
            intent.putExtra("type", false);
        } else if (clickedTask instanceof RecurrentTask) {
            intent.putExtra("type", true);
        }
        intent.putExtra("taskId", clickedTask.getId());
        startActivity(intent);
    }
    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, dayOfMonth, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);

        long startOfDay = cal.getTimeInMillis();

        cal.set(year, month, dayOfMonth, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);

        long endOfDay = cal.getTimeInMillis();

        getTasksForDay(startOfDay, endOfDay);


    }

    private void getTasksForDay(long start, long end) {

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
//        if (snapshot.exists()){
//            if (!isRecurring){
//                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
//
//                    NonRecurrentTask ntask = taskSnapshot.getValue(NonRecurrentTask.class);
//
//                    if (ntask != null) {
//                        ntask.setId(taskSnapshot.getKey());
//                    }
//                    taskList.add(ntask);
//                }
//
//                System.out.println(taskList);
//            }
//            else{
//                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
//
//                    RecurrentTask rtask = taskSnapshot.getValue(RecurrentTask.class);
//
//                    if (rtask != null) {
//                        rtask.setId(taskSnapshot.getKey());
//                    }
//                    taskList.add(rtask);
//                }
//
//                System.out.println(taskList);
//            }
//        }else{
//            Toast.makeText(this, "0 tasks", Toast.LENGTH_LONG).show();
//        }
        if (snapshot.exists()){
            for (DataSnapshot ds : snapshot.getChildren()) {
                taskIds.put(ds.getKey(), "NonRecurrent");
            }
        }
        else{
            Toast.makeText(CalendarActivity.this,
                    "0 tasks",
                    Toast.LENGTH_LONG).show();
        }

        Log.d("TASK_IDS", taskIds.toString());
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        System.out.println("Error: " + error.getMessage());
        //Toast.makeText(this, "Error: ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }
}
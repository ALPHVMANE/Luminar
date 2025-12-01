package com.example.luminar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.*;
import services.NavigationHelper;

public class CalendarActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, CalendarView.OnDateChangeListener {
    CalendarView calendar;
    TextView calendarDate;
    ListView lvTasks;
    ArrayList<Task> currentDateTasks;
    TaskAdapter taskAdapter;
    private long selectedDateMillis;
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

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_calendar);
        NavigationHelper.setupBottomNavigation(this, bottomNav, R.id.nav_calendar);
//        BottomSheetDialog btmSheet = new BottomSheetDialog(BottomSheetActivity.class);
        currentDateTasks = new ArrayList<>();
        calendar.setOnDateChangeListener(this);

        lvTasks.setOnItemClickListener(this);
        taskAdapter = new TaskAdapter(this, currentDateTasks);
        lvTasks.setAdapter(taskAdapter);
        taskAdapter.notifyDataSetChanged();
    }

    private void loadNonRecurrent(DataSnapshot snapshot) {
        long dayStart = selectedDateMillis;
        long dayEnd = selectedDateMillis + (24 * 60 * 60 * 1000) - 1;

        for (DataSnapshot ds : snapshot.getChildren()) {
            NonRecurrentTask nt = ds.getValue(NonRecurrentTask.class);

            if (nt != null && nt.getDueDate() >= dayStart && nt.getDueDate() <= dayEnd) {
                nt.setId(ds.getKey());
                currentDateTasks.add(nt);
            }
        }

        taskAdapter.notifyDataSetChanged();
    }

    private void loadRecurrent(DataSnapshot snapshot) {
        long dayStart = selectedDateMillis;
        long dayEnd = selectedDateMillis + (24 * 60 * 60 * 1000) - 1;

        for (DataSnapshot ds : snapshot.getChildren()) {
            RecurrentTask rt = ds.getValue(RecurrentTask.class);

            if (rt != null && rt.getStartCalendar() >= dayStart && rt.getStartCalendar() <= dayEnd) {
                rt.setId(ds.getKey());
                currentDateTasks.add(rt);
            }
        }

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

        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
        calendarDate.setText(date);

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, dayOfMonth, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        selectedDateMillis = cal.getTimeInMillis();

        currentDateTasks.clear();

        // Remove previous listeners to avoid duplication
        nTasksDB.child(Global.getUid()).removeEventListener(nonRecurrentListener);
        rTasksDB.child(Global.getUid()).removeEventListener(recurrentListener);

        // Add fresh listeners
        nTasksDB.child(Global.getUid()).addValueEventListener(nonRecurrentListener);
        rTasksDB.child(Global.getUid()).addValueEventListener(recurrentListener);


    }

    private final ValueEventListener nonRecurrentListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            loadNonRecurrent(snapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) { }
    };

    private final ValueEventListener recurrentListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            loadRecurrent(snapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) { }
    };
}
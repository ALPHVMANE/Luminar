package com.example.luminar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.UUID;

import model.*;
import services.NavigationHelper;
import services.NotificationScheduler;

public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtName, edtNotes, edtGoalDate, edtStartTime, edtEndTime;
    Button btnSubmit;
    Spinner spnCategory, spnFrequency;
    CheckBox checkBoxRecurrent, chkNotifications;
    Calendar goalDateCalendar, startTimeCalendar, endTimeCalendar;

    // Set the categories (set hex colors)
    Category[] categories = {
            new Category(1, "Personal", "FF6B6B"),
            new Category(2, "Work", "4ECDC4"),
            new Category(3, "Health", "93E1D3"),
            new Category(4, "Education", "F38181"),
            new Category(5, "Finance", "AA96DA")
    };

    // Set the frequencies
    Frequency[] frequencies = {
            Frequency.DAILY,
            Frequency.WEEKLY,
            Frequency.MONTHLY,
            Frequency.YEARLY
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        // Fixes previous error of page not loading
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            EdgeToEdge.enable(this);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
        initialize();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSubmit) {
            saveTask(v);
        } else if (id == R.id.edtGoalDate) {
            showDatePicker();
        } else if (id == R.id.edtStartTime) {
            showTimePicker(true);
        } else if (id == R.id.edtEndTime) {
            showTimePicker(false);
        }
    }

    private void initialize() {
        edtName = findViewById(R.id.edtName);
        edtNotes = findViewById(R.id.edtNotes);
        edtGoalDate = findViewById(R.id.edtGoalDate);
        edtStartTime = findViewById(R.id.edtStartTime);
        edtEndTime = findViewById(R.id.edtEndTime);
        btnSubmit = findViewById(R.id.btnSubmit);
        spnCategory = findViewById(R.id.spnCategory);
        spnFrequency = findViewById(R.id.spnFrequency);
        checkBoxRecurrent = findViewById(R.id.checkBox);
        chkNotifications = findViewById(R.id.chkNotifications);
        btnSubmit.setOnClickListener(this);
        edtGoalDate.setOnClickListener(this);
        edtStartTime.setOnClickListener(this);
        edtEndTime.setOnClickListener(this);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_calendar);
        NavigationHelper.setupBottomNavigation(this, bottomNav, R.id.nav_add_task);

        // Ensure date/time cannot be edited by keyboard, only by Calendar itself
        // + initialize calendars
        edtGoalDate.setFocusable(false);
        edtStartTime.setFocusable(false);
        edtEndTime.setFocusable(false);
        goalDateCalendar = Calendar.getInstance();
        startTimeCalendar = Calendar.getInstance();
        endTimeCalendar = Calendar.getInstance();

        // Spinners
        setupCategorySpinner();
        setupFrequencySpinner();

        // Checkboxes
        checkBoxRecurrent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            spnFrequency.setEnabled(isChecked);
            edtStartTime.setEnabled(isChecked);
            edtEndTime.setEnabled(isChecked);
            if (!isChecked) {
                spnFrequency.setSelection(0);
            }
        });

        spnFrequency.setEnabled(false);
    }

    private void setupCategorySpinner() {
        String[] category = new String[categories.length];
        for (int i = 0; i < categories.length; i++) {
            category[i] = categories[i].getName();
        }

        // Set into ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                category
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);
    }

    private void setupFrequencySpinner() {
        String[] freq = new String[frequencies.length];
        for (int i = 0; i < frequencies.length; i++) {
            freq[i] = frequencies[i].name();
        }

        // Set into ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                freq
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFrequency.setAdapter(adapter);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year, month, day;
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    goalDateCalendar.set(selectedYear, selectedMonth, selectedDay);
                    String displayDate = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;
                    edtGoalDate.setText(displayDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void showTimePicker(boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        int hour, minute;
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    String displayTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                    if (isStartTime) {
                        startTimeCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        startTimeCalendar.set(Calendar.MINUTE, selectedMinute);
                        edtStartTime.setText(displayTime);
                    } else {
                        endTimeCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        endTimeCalendar.set(Calendar.MINUTE, selectedMinute);
                        edtEndTime.setText(displayTime);
                    }
                },
                hour, minute, true
        );
        timePickerDialog.show();
    }

    private void saveTask(View v) {
        try {
            // Get inputs
            String name, notes, goalDate;
            name = edtName.getText().toString().trim();
            notes = edtNotes.getText().toString().trim();
            goalDate = edtGoalDate.getText().toString().trim();

            boolean isRecurrent, enableNotif;
            isRecurrent = checkBoxRecurrent.isChecked();
            enableNotif = chkNotifications.isChecked();

            // Validation
            if (name.isEmpty()) {
                Snackbar.make(v, "Please enter a task name", Snackbar.LENGTH_LONG).show();
                return;
            }

            if (goalDate.isEmpty()) {
                Snackbar.make(v, "Please select a goal date", Snackbar.LENGTH_LONG).show();
                return;
            }

            // Get category
            int categoryPosition = spnCategory.getSelectedItemPosition();
            Category selectedCategory = categories[categoryPosition];

            // User ID
            String taskId = UUID.randomUUID().toString();
            String userId = Global.getUid();
            if (userId == null || userId.isEmpty()) {
                Snackbar.make(v, "User not logged in", Snackbar.LENGTH_LONG).show();
                return;
            }

            // Timestamp
            long now = System.currentTimeMillis();

            // Default values
            Status status = Status.TODO;
            Priority priority = Priority.MEDIUM;

            if (isRecurrent) {
                int freqPosition = spnFrequency.getSelectedItemPosition();
                Frequency selectedFrequency = frequencies[freqPosition];

                String startTime = edtStartTime.getText().toString().trim();
                String endTime = edtEndTime.getText().toString().trim();

                if (startTime.isEmpty() || endTime.isEmpty()) {
                    Snackbar.make(v, "Please set start/end times for recurrent tasks", Snackbar.LENGTH_LONG).show();
                    return;
                }

                // Convert calendar to long
                long startTimeMillis = startTimeCalendar.getTimeInMillis();
                long endTimeMillis = endTimeCalendar.getTimeInMillis();
                long goalDateMillis = goalDateCalendar.getTimeInMillis();

                // Add tasks based on frequency
                int tasksCreated = createRecurrentTasks(name, notes, selectedCategory, status, userId, priority,
                        enableNotif, now, selectedFrequency, startTimeMillis, endTimeMillis, goalDateMillis
                );

                Snackbar.make(v, "Recurrent task '" + name + "' created successfully", Snackbar.LENGTH_LONG).show();
                finish();
            } else {
                // Convert Calendar to Long
                // Convert Calendar to Long for due date (end of day: 23:59:59)
                Calendar dueDateCal = (Calendar) goalDateCalendar.clone();
                dueDateCal.set(Calendar.HOUR_OF_DAY, 23);
                dueDateCal.set(Calendar.MINUTE, 59);
                dueDateCal.set(Calendar.SECOND, 59);
                dueDateCal.set(Calendar.MILLISECOND, 999);
                long dueDateMillis = dueDateCal.getTimeInMillis();


                // Non-recurring tasks
                NonRecurrentTask task = new NonRecurrentTask(
                        taskId, name, notes, selectedCategory, status, userId, priority, enableNotif, now, now, dueDateMillis
                );

                task.save(task);

                if(enableNotif){
                    scheduleNonRecurrentNotification(taskId, name, notes, dueDateMillis);
                }

                Snackbar.make(v, "Task '" + name + "' created successfully", Snackbar.LENGTH_LONG).show();
                finish();
            }

            // Clear all
            resetWidgets();
        } catch (Exception e) {
            Log.e("AddTaskActivity", "Error saving task: " + e.getMessage());
            e.printStackTrace();
            Snackbar.make(v, "Error creating task: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    private int createRecurrentTasks(String name, String notes, Category category, Status status, String userId, Priority priority, boolean enableNotif,
                                     long now, Frequency freq, long startTimeMillis, long endTimeMillis, long goalDateMillis) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTimeInMillis(now);
        // Reset to start of day
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);

        Calendar endDate = Calendar.getInstance();
        endDate.setTimeInMillis(goalDateMillis);
        int tasksCreated = 0;

        // Create tasks until end date (INCLUSIVE)
        while (currentDate.getTimeInMillis() <= endDate.getTimeInMillis()) {
            String taskId = UUID.randomUUID().toString();
            Calendar instanceDueDate = (Calendar) currentDate.clone();

            // Set time from startTimeCalendar
            instanceDueDate.set(Calendar.HOUR_OF_DAY, startTimeCalendar.get(Calendar.HOUR_OF_DAY));
            instanceDueDate.set(Calendar.MINUTE, startTimeCalendar.get(Calendar.MINUTE));
            instanceDueDate.set(Calendar.SECOND, 0);
            instanceDueDate.set(Calendar.MILLISECOND, 0);
            long instanceDueDateMillis = instanceDueDate.getTimeInMillis();

            // Create start time for this specific instance
            Calendar instanceStartTime = (Calendar) instanceDueDate.clone();
            instanceStartTime.set(Calendar.HOUR_OF_DAY, startTimeCalendar.get(Calendar.HOUR_OF_DAY));
            instanceStartTime.set(Calendar.MINUTE, startTimeCalendar.get(Calendar.MINUTE));
            long instanceStartTimeMillis = instanceStartTime.getTimeInMillis();

            // Create end time for this specific instance
            Calendar instanceEndTime = (Calendar) instanceDueDate.clone();
            instanceEndTime.set(Calendar.HOUR_OF_DAY, endTimeCalendar.get(Calendar.HOUR_OF_DAY));
            instanceEndTime.set(Calendar.MINUTE, endTimeCalendar.get(Calendar.MINUTE));
            long instanceEndTimeMillis = instanceEndTime.getTimeInMillis();

            // Set the task with instance-specific times
            RecurrentTask task = new RecurrentTask(
                    taskId, name, notes, category, status, userId, priority, enableNotif,
                    now, now, freq, instanceStartTimeMillis, instanceEndTimeMillis, instanceDueDateMillis
            );

            // Save + Create task
            task.save(task);

            if(enableNotif){
                scheduleRecurrentNotification(taskId, name, notes, instanceDueDateMillis);
            }

            tasksCreated++;

            // Move to next occurrence based on freq
            switch (freq) {
                case DAILY:
                    currentDate.add(Calendar.DAY_OF_MONTH, 1);
                    break;
                case WEEKLY:
                    currentDate.add(Calendar.WEEK_OF_YEAR, 1);
                    break;
                case MONTHLY:
                    currentDate.add(Calendar.MONTH, 1);
                    break;
                case YEARLY:
                    currentDate.add(Calendar.YEAR, 1);
                    break;
            }
        }

        // LogCat
        Log.d("AddTaskActivity", "Created " + tasksCreated + " recurrent tasks with the following frequency:" + freq);
        return tasksCreated;
    }

    private void resetWidgets() {
        edtName.setText(null);
        edtNotes.setText(null);
        edtGoalDate.setText(null);
        edtStartTime.setText(null);
        edtEndTime.setText(null);
        spnCategory.setSelection(0);
        spnFrequency.setSelection(0);
        checkBoxRecurrent.setChecked(false);
        chkNotifications.setChecked(false);
        goalDateCalendar = Calendar.getInstance();
        startTimeCalendar = Calendar.getInstance();
        endTimeCalendar = Calendar.getInstance();
    }

    private void scheduleNonRecurrentNotification(String taskId, String name, String notes, long time){
        //Calculate 1 day before the due date
        long oneDayBefore = time - (9*60*60*1000);

        //Only schedule if notification time is in the future
        if(oneDayBefore > System.currentTimeMillis()){
            Log.d("AddTaskActivity", "Scheduling notification for non-recurrent task:");
            String notificationMessage = "Your task '" + name.toLowerCase() + "' is due tomorrow at " + DateConverter.convertMillisToFormattedDate(time) + ".";
            NotificationScheduler.scheduleTaskReminder(
                    this,
                    taskId,
                    name,
                    notificationMessage,
                    oneDayBefore
            );
            saveNotification(taskId, name, notificationMessage, oneDayBefore);
            Log.d("AddTaskActivity", "Scheduled notification for non-recurrent task: " + name +
                    " at " + new java.util.Date(oneDayBefore));
        }else{
            Log.d("AddTaskActivity", "Cannot schedule notification for non-recurrent task: " + name);
        }
    }

    private void scheduleRecurrentNotification(String taskId, String name, String notes, long time) {
        long thirtyMinBefore = time - (30 * 60 * 1000);
        if(thirtyMinBefore > System.currentTimeMillis()) {
            String notificationMessage = "Your task '" + name.toLowerCase() + "' is due in 30 minutes!";
            NotificationScheduler.scheduleTaskReminder(
                    this,
                    taskId,
                    name,
                    notificationMessage,
                    thirtyMinBefore
            );

            saveNotification(taskId, name, notificationMessage, thirtyMinBefore);
            Log.d("AddTaskActivity", "Scheduled notification for recurrent task: " + name +
                    " at " + new java.util.Date(thirtyMinBefore));
        }else{
            Log.d("AddTaskActivity", "Notification time is in the past for task: " + name);
        }
    }

    private void saveNotification(String taskId, String name, String notes, long time) {
        String notificationId = UUID.randomUUID().toString();
        String userId = Global.getUid();

        Notification notification = new Notification(
                notificationId,  // Include the ID as first parameter
                true,
                taskId,
                userId,
                notes,
                name,
                time
        );

        notification.save(notification);
        Log.d("AddTaskActivity", "Notification saved to Firebase: " + notificationId);
    }
}
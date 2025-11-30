package com.example.luminar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.UUID;

import model.*;

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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_addtask);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
            LocalDateTime now = LocalDateTime.now();

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

                RecurrentTask task = new RecurrentTask(
                        taskId, name, notes, selectedCategory, status, userId, priority, enableNotif, now, now, selectedFrequency, (Calendar) startTimeCalendar.clone(), (Calendar) endTimeCalendar.clone(), (Calendar) goalDateCalendar.clone()
                );

                task.save(task);
                Snackbar.make(v, "Recurrent task '" + name + "' created successfully", Snackbar.LENGTH_LONG).show();
            } else {
                // Non-recurring tasks
                NonRecurrentTask task = new NonRecurrentTask(
                        taskId, name, notes, selectedCategory, status, userId, priority, enableNotif, now, now, (Calendar) goalDateCalendar.clone()
                );

                task.save(task);
                Snackbar.make(v, "Task '" + name + "' created successfully", Snackbar.LENGTH_LONG).show();
            }

            // Clear all
            resetWidgets();
        } catch (Exception e) {
            Log.e("AddTaskActivity", "Error saving task: " + e.getMessage());
            e.printStackTrace();
            Snackbar.make(v, "Error creating task: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
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


}

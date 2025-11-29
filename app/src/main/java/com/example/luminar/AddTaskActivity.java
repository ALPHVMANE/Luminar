//package com.example.luminar;
//
//import android.app.DatePickerDialog;
//import android.app.TimePickerDialog;
//import android.graphics.Color;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.time.LocalDateTime;
//import java.util.Calendar;
//import java.util.UUID;
//
//import model.Category;
//import model.Frequency;
//import model.Global;
//import model.NonRecurrentTask;
//import model.Priority;
//import model.RecurrentTask;
//import model.Status;
//
//public class AddTaskActivity extends AppCompatActivity {
//
//    private EditText edtName, edtNotes, edtGoalDate, edtStartTime, edtEndTime, edtFrequency, edtReminder, edtPickColor;
//    private Button btnSubmit;
//
//    private Calendar selectedGoalDate;
//        private Calendar selectedStartTime;
//        private Calendar selectedEndTime;
//        private Frequency selectedFrequency;
//        private String selectedColor = "#4CAF50"; // Default color
//        private Category selectedCategory = new Category(1, "Work", "#4CAF50"); // Default category
//        private Priority selectedPriority = Priority.MEDIUM; // Default priority
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_addtask);
//
//            initializeViews();
//            setupClickListeners();
//        }
//
//        private void initializeViews() {
//        edtName = findViewById(R.id.edtName);
//        edtNotes = findViewById(R.id.edtNotes);
//        edtGoalDate = findViewById(R.id.edtGoalDate);
//        edtStartTime = findViewById(R.id.edtStartTime);
//        edtEndTime = findViewById(R.id.edtEndTime);
//        edtFrequency = findViewById(R.id.edtFrequency);
//        edtReminder = findViewById(R.id.edtReminder);
//        edtPickColor = findViewById(R.id.edtPickColor);
//        btnSubmit = findViewById(R.id.btnSubmit);
//
//        // Make EditTexts non-editable (they open dialogs instead)
//        edtGoalDate.setFocusable(false);
//        edtStartTime.setFocusable(false);
//        edtEndTime.setFocusable(false);
//        edtFrequency.setFocusable(false);
//        edtPickColor.setFocusable(false);
//    }
//
//    private void setupClickListeners() {
//        // Goal Date Picker
//        edtGoalDate.setOnClickListener(v -> showDatePicker());
//
//        // Start Time Picker
//        edtStartTime.setOnClickListener(v -> showTimePicker(true));
//
//        // End Time Picker
//        edtEndTime.setOnClickListener(v -> showTimePicker(false));
//
//        // Frequency Selector
//        edtFrequency.setOnClickListener(v -> showFrequencyDialog());
//
//        // Color Picker
//        edtPickColor.setOnClickListener(v -> showColorPicker());
//
//        // Reminder (could be expanded to show time picker for reminder time)
//        edtReminder.setOnClickListener(v -> {
//            Toast.makeText(this, "Reminder feature - coming soon", Toast.LENGTH_SHORT).show();
//        });
//
//        // Submit Button
//        btnSubmit.setOnClickListener(v -> saveTask());
//    }
//
//    private void showDatePicker() {
//        Calendar calendar = Calendar.getInstance();
//        DatePickerDialog datePickerDialog = new DatePickerDialog(
//                this,
//                (view, year, month, dayOfMonth) -> {
//                    selectedGoalDate = Calendar.getInstance();
//                    selectedGoalDate.set(year, month, dayOfMonth);
//                    edtGoalDate.setText(String.format("%02d/%02d/%d", dayOfMonth, month + 1, year));
//                },
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)
//        );
//        datePickerDialog.show();
//    }

//    private void showTimePicker(boolean isStartTime) {
//        Calendar calendar = Calendar.getInstance();
//        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
//                    Calendar timeCalendar = Calendar.getInstance();
//                    timeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                    timeCalendar.set(Calendar.MINUTE, minute);
//
//                    String timeString = String.format("%02d:%02d", hourOfDay, minute);
//
//                    if (isStartTime) {
//                        selectedStartTime = timeCalendar;
//                        edtStartTime.setText(timeString);
//                    } else {
//                        selectedEndTime = timeCalendar;
//                        edtEndTime.setText(timeString);
//                    }
//                },
//                calendar.get(Calendar.HOUR_OF_DAY),
//                calendar.get(Calendar.MINUTE),
//                true
//        );
//        timePickerDialog.show();
//    }

//    private void showFrequencyDialog() {
//        String[] frequencies = {"None (One-time task)", "Daily", "Weekly", "Monthly", "Yearly"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Select Frequency")
//                .setItems(frequencies, (dialog, which) -> {
//                    switch (which) {
//                        case 0:
//                            selectedFrequency = null;
//                            edtFrequency.setText("One-time task");
//                            break;
//                        case 1:
//                            selectedFrequency = Frequency.DAILY;
//                            edtFrequency.setText("Daily");
//                            break;
//                        case 2:
//                            selectedFrequency = Frequency.WEEKLY;
//                            edtFrequency.setText("Weekly");
//                            break;
//                        case 3:
//                            selectedFrequency = Frequency.MONTHLY;
//                            edtFrequency.setText("Monthly");
//                            break;
//                        case 4:
//                            selectedFrequency = Frequency.YEARLY;
//                            edtFrequency.setText("Yearly");
//                            break;
//                    }
//                })
//                .show();
//    }
//    private void saveTask() {
//        // Validate
//        String taskName = edtName.getText().toString().trim();
//        if (taskName.isEmpty()) {
//            Toast.makeText(this, "Please enter a task name", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String taskNotes = edtNotes.getText().toString().trim();
//        String taskId = UUID.randomUUID().toString();
//        String userId = Global.getUid();
//        LocalDateTime now = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            now = LocalDateTime.now();
//        }
//
//        // Check the type of task (recurring or non-recurring)
//        // Recurring task
//        if (selectedFrequency != null && selectedStartTime != null) {
//            RecurrentTask recurrentTask = new RecurrentTask(taskId, taskName, taskNotes, selectedCategory, Status.TODO, userId, selectedPriority, true, now, now);
//
//            recurrentTask.setFreq(selectedFrequency);
//            recurrentTask.setStartCalendar(selectedStartTime);
//            recurrentTask.setEndCalendar(selectedEndTime != null ? selectedEndTime : selectedStartTime);
//            recurrentTask.setNextOccurence(selectedStartTime);
//
//            // Save to Firebase
//            recurrentTask.save(recurrentTask);
//
//            Toast.makeText(this, "Recurrent task saved successfully!", Toast.LENGTH_SHORT).show();
//            Log.d("AddTask", "Recurrent task created: " + taskName);
//
//            // Non-recurrent task
//        } else if (selectedGoalDate != null) {
//            NonRecurrentTask nonRecurrentTask = new NonRecurrentTask(
//                    taskId,
//                    taskName,
//                    taskNotes,
//                    selectedCategory,
//                    Status.TODO,
//                    userId,
//                    selectedPriority,
//                    true, // enableNotif
//                    now,
//                    now
//            );
//
//            nonRecurrentTask.setDueDate(selectedGoalDate);
//
//            // Save to Firebase
//            nonRecurrentTask.save(nonRecurrentTask);
//
//            Toast.makeText(this, "Task saved successfully!", Toast.LENGTH_SHORT).show();
//            Log.d("AddTask", "Non-recurrent task created: " + taskName);
//
//        } else {
//            Toast.makeText(this, "Please select either a goal date or frequency", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Close activity after saving
//        finish();
//    }
//}

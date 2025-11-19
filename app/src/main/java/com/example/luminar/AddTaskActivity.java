package com.example.luminar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    private EditText edtName, edtNotes, edtGoalDate, edtStartTime, edtEndTime,
            edtFrequency, edtReminder, edtPickColor;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask); // Assume your XML is named activity_add_task.xml

        edtName = findViewById(R.id.edtName);
        edtNotes = findViewById(R.id.edtNotes);
        edtGoalDate = findViewById(R.id.edtGoalDate);
        edtStartTime = findViewById(R.id.edtStartTime);
        edtEndTime = findViewById(R.id.edtEndTime);
        edtFrequency = findViewById(R.id.edtFrequency);
        edtReminder = findViewById(R.id.edtReminder);
        edtPickColor = findViewById(R.id.edtPickColor);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Date picker for goal date
        edtGoalDate.setFocusable(false);
        edtGoalDate.setClickable(true);
        edtGoalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(edtGoalDate);
            }
        });

        // Time picker for start time
        edtStartTime.setFocusable(false);
        edtStartTime.setClickable(true);
        edtStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(edtStartTime);
            }
        });

        // Time picker for end time
        edtEndTime.setFocusable(false);
        edtEndTime.setClickable(true);
        edtEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(edtEndTime);
            }
        });

        // Handle button submit (example: retrieve all inputs)
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String notes = edtNotes.getText().toString();
                String goalDate = edtGoalDate.getText().toString();
                String startTime = edtStartTime.getText().toString();
                String endTime = edtEndTime.getText().toString();
                String frequency = edtFrequency.getText().toString();
                String reminder = edtReminder.getText().toString();
                String pickColor = edtPickColor.getText().toString();

                // TODO: Add logic to save/use entered task data
            }
        });
    }

    private void showDatePickerDialog(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int y, int m, int d) {
                        String date = String.format("%02d/%02d/%04d", d, m + 1, y);
                        editText.setText(date);
                    }
                }, year, month, day);
        dialog.show();
    }

    private void showTimePickerDialog(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int h, int m) {
                        String time = String.format("%02d:%02d", h, m);
                        editText.setText(time);
                    }
                }, hour, minute, true);
        dialog.show();
    }
}

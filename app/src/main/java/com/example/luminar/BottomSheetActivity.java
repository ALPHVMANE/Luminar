package com.example.luminar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import model.NonRecurrentTask;
import model.RecurrentTask;

public class BottomSheetActivity extends AppCompatActivity {
    TextView txtTitle, txtDescription, txtCategory, txtStatus, txtDueDate, txtFreq, txtStart, txtNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bottom_sheet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initialize();
    }
    public void initialize(){
        String taskId = getIntent().getStringExtra("taskId");
        txtTitle = findViewById(R.id.tvTitle);
        txtDescription = findViewById(R.id.txtDesc);
        txtCategory = findViewById(R.id.txtCat);
        txtStatus = findViewById(R.id.txtStat);
//        txtPriority = findViewById(R.id.txtPrio);

        txtDueDate = findViewById(R.id.txtDueDate);

        txtFreq = findViewById(R.id.txtFreq);
        txtStart = findViewById(R.id.txtStartDate);
        txtNext = findViewById(R.id.txtNextOcurrence);

        String id = getIntent().getStringExtra("taskId");
        boolean isRecurring = getIntent().getBooleanExtra("type", false);


        if (isRecurring) {

            RecurrentTask.load(id, task -> {
                if (task == null) {return;}

                //Visibility
                txtFreq.setVisibility(View.VISIBLE);
                txtStart.setVisibility(View.VISIBLE);
                txtNext.setVisibility(View.VISIBLE);
                txtDueDate.setVisibility(View.GONE);

                txtTitle.setText(task.getTitle());
                txtDescription.setText(task.getDescription());
                txtCategory.setText(task.getCategory().toString());
                txtStatus.setText(task.getStatus().toString());
//                txtPriority.setText(task.getPriority().toString());

                txtFreq.setText(task.getFreq().toString());
                txtStart.setText(task.getStartCalendar().toString());
                txtNext.setText(task.getNextOccurence().toString());
            });

        } else {

            NonRecurrentTask.load(id, task -> {
                if (task == null) {return;}

                //Visibility
                txtFreq.setVisibility(View.GONE);
                txtStart.setVisibility(View.GONE);
                txtNext.setVisibility(View.GONE);
                txtDueDate.setVisibility(View.VISIBLE);

                txtTitle.setText(task.getTitle());
                txtDescription.setText(task.getDescription());
                txtCategory.setText(task.getCategory().toString());
                txtStatus.setText(task.getStatus().toString());
//                txtPriority.setText(task.getPriority().toString());

                txtDueDate.setText(task.getDueDate().toString());
            });
        }
    }
}
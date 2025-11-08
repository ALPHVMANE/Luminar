package com.example.prjluminar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdvanceActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);
        
        Button next = findViewById(R.id.btnNextAdvance);
        next.setOnClickListener(v ->
                startActivity(new Intent(this, FollowActivity.class))
        );

        TextView prev = findViewById(R.id.txtPrev);
        prev.setOnClickListener(v -> {
            Intent intent = new Intent(AdvanceActivity.this, RoutineActivity.class);
            startActivity(intent);
            finish(); // optional – prevents stacking
        });
    }
}

package com.example.luminar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button next = findViewById(R.id.btnNextWelcome);
        next.setOnClickListener(v ->
                startActivity(new Intent(WelcomeActivity.this, AddTaskActivity.class))
        );
    }
}

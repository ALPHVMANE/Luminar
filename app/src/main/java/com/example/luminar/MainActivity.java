package com.example.luminar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Auto-advance to Welcome after a short delay
        new Handler().postDelayed(() -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            finish();
        }, 1200);
    }
}

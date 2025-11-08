package com.example.prjluminar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText edtLoginEmail = findViewById(R.id.edtLoginEmail);
        EditText edtLoginPassword = findViewById(R.id.edtLoginPassword);
        Button btnAccess = findViewById(R.id.btnAccess);

        btnAccess.setOnClickListener(v -> {
            String email = edtLoginEmail.getText().toString().trim();
            String password = edtLoginPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Simulate login success and go to Welcome
            Intent i = new Intent(LoginActivity.this, WelcomeActivity.class);
            startActivity(i);
            finish(); // optional: prevents going back to Login
        });
    }
}

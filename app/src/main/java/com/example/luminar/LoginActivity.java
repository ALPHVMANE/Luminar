package com.example.luminar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import model.Global;
import model.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edEmail, edPassword;
    Button btnLogin;
    TextView tvRegister;

    private final User user = new User();
    private FirebaseAuth mAuth;

    //Database References
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // If Firebase Auth session exists, skip login
        if (mAuth.getCurrentUser() != null) {
            Global.setUid(mAuth.getCurrentUser().getUid());
            goToMain();
        }
    }

    private void goToMain() {
        startActivity(new Intent(LoginActivity.this, CalendarActivity.class));
        finish();
    }

    private void initialize() {
        edEmail = findViewById(R.id.edtLoginEmail);
        edPassword = findViewById(R.id.edtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister= findViewById(R.id.tvRegister_Login);
        tvRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnLogin){
            String email = edEmail.getText().toString().trim();
            String password = edPassword.getText().toString().trim();
            loginUser(email, password);
        }
        if(v.getId() == R.id.tvRegister_Login){
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        }
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.login(this, email);
                        goToMain();
                    }
                    else{
                        Toast.makeText(LoginActivity.this,
                                "Login failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}

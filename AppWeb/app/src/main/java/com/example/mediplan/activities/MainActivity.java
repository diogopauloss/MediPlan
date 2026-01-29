package com.example.mediplan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mediplan.R;
import com.example.mediplan.managers.AuthManager;

public class MainActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link XML IDs to Java variables
        initViews();
        // Initialize the Logic Managers
        setupManagers();
        // Configure Button Clicks
        setupListeners();
    }

    private void initViews() {
        editEmail = findViewById(R.id.activity_main_Email_edit_text_id);
        editPassword = findViewById(R.id.activity_main_Password_edit_text_id);
        btnLogin = findViewById(R.id.activity_main_Login_button_id);
        tvRegister = findViewById(R.id.activity_main_Create_Account_text_view_id);
    }

    private void setupManagers() {
        // Initialize AuthManager to handle Login communication
        authManager = new AuthManager(this);
    }

    private void setupListeners() {
        // Button to open Create Account screen
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, CreateAccountActivity.class));
        });

        // Button to perform Login
        btnLogin.setOnClickListener(v -> performLogin());
    }

    private void performLogin() {
        // Get text from inputs and remove extra spaces
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // Simple Validation: Check if empty
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preenche os dados todos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call Manager to talk to API
        authManager.login(email, password, new AuthManager.AuthListener() {
            @Override
            public void onSuccess() {
                // Login OK: Show welcome and go to Home
                Toast.makeText(MainActivity.this, "Bem-vindo!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish(); // Close Login screen so user cannot go back
            }

            @Override
            public void onError(String message) {
                // Login Failed: Show error message
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
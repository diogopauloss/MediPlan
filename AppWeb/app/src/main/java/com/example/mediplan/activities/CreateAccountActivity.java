package com.example.mediplan.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mediplan.managers.AuthManager;
import com.example.mediplan.R;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnSignUp;
    private TextView tvLoginLink;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Link XML IDs to Java variables
        initViews();
        // Initialize the Logic Managers
        setupManagers();
        // Configure Button Clicks
        setupListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.activity_create_account_Name_edit_text_id);
        etEmail = findViewById(R.id.activity_create_account_Email_edit_text_id);
        etPassword = findViewById(R.id.activity_create_account_Password_edit_text_id);
        btnSignUp = findViewById(R.id.activity_create_account_Sign_Up_button_id);
        tvLoginLink = findViewById(R.id.activity_create_account_Login_text_view_id);
    }

    private void setupManagers() {
        // Initialize AuthManager to handle Registration communication
        authManager = new AuthManager(this);
    }

    private void setupListeners() {
        // Link to go back to Login screen
        tvLoginLink.setOnClickListener(v -> finish());

        // Button to perform Registration
        btnSignUp.setOnClickListener(v -> performRegister());
    }

    private void performRegister() {
        // Get text from inputs and remove extra spaces
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Simple Validation: Check if empty
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preenche tudo, por favor!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call Manager to talk to API
        authManager.register(name, email, password, new AuthManager.AuthListener() {
            @Override
            public void onSuccess() {
                // Registration OK: Show success message and close screen
                Toast.makeText(CreateAccountActivity.this, "Conta criada! Faz login.", Toast.LENGTH_LONG).show();
                finish(); // Returns to the Login Screen
            }

            @Override
            public void onError(String message) {
                // Registration Failed: Show error message
                Toast.makeText(CreateAccountActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
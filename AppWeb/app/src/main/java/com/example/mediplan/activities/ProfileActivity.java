package com.example.mediplan.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mediplan.R;
import com.example.mediplan.managers.AuthManager;

public class ProfileActivity extends AppCompatActivity {
    private TextView tvProfileName;
    private Button btnLogout, btnDeleteAccount;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Link XML IDs to Java variables
        initViews();
        // Initialize the Logic Managers
        setupManagers();
        // Setup the User Interface (Show name)
        setupUI();
        // Configure Button Clicks
        setupListeners();
    }
    private void initViews() {
        tvProfileName = findViewById(R.id.activity_profile_ProfileName_text_view_id);
        btnLogout = findViewById(R.id.activity_profile_Logout_button_id);
        btnDeleteAccount = findViewById(R.id.activity_profile_DeleteAccount_button_id);
    }

    private void setupManagers() {
        // Initialize AuthManager to handle account operations
        authManager = new AuthManager(this);
    }

    private void setupUI() {
        // Get user data from local memory (SharedPreferences)
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String name = prefs.getString("USER_NAME", "User");

        // Update the TextView with the user's name
        tvProfileName.setText(name);
    }

    private void setupListeners() {
        // Handle Logout button click
        btnLogout.setOnClickListener(v -> performLogout());

        // Handle Delete Account button click
        btnDeleteAccount.setOnClickListener(v -> confirmDeleteAccount());
    }

    private void performLogout() {
        // Clear local memory (Session)
        getSharedPreferences("user_session", MODE_PRIVATE).edit().clear().apply();

        // Go back to Login screen
        goToLogin();
    }

    // Function to ask for confirmation before deleting
    private void confirmDeleteAccount() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure? You will lose all your plans!")
                .setPositiveButton("Yes, delete", (dialog, which) -> {
                    // Get User ID from memory
                    int userId = getSharedPreferences("user_session", MODE_PRIVATE).getInt("USER_ID", -1);

                    // Call API to delete user
                    performDelete(userId);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Function to call the Manager and delete the user
    private void performDelete(int userId) {
        authManager.deleteUser(userId, new AuthManager.DeleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(ProfileActivity.this, "Account deleted successfully.", Toast.LENGTH_LONG).show();

                // Clear memory and exit
                performLogout();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToLogin() {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        // Clear activity stack (User cannot go back)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
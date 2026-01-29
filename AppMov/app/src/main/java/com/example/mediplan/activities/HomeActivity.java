package com.example.mediplan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediplan.R;
import com.example.mediplan.adapters.PlanAdapter;
import com.example.mediplan.managers.PlanManager;
import com.example.mediplan.models.Plan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvDailyMeds;
    private FloatingActionButton fabAdd;
    private TextView tvGreeting, tvSubtitle;
    private View btnProfile;
    private TextView tvWeekDay1, tvMonthDay1, tvWeekDay2, tvMonthDay2, tvWeekDay3, tvMonthDay3;
    private PlanManager planManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Link XML IDs to Java variables
        initViews();
        // Initialize the Logic Managers
        setupManagers();
        // Configure Visuals (List Layout & Calendar)
        setupUI();
        // Configure Button Clicks
        setupListeners();
        // Check if user is logged in and load their data
        checkSessionAndLoad();
    }

    private void initViews() {
        // Connect generic UI components
        rvDailyMeds = findViewById(R.id.activity_home_DailyMeds_recycler_view_id);
        fabAdd = findViewById(R.id.activity_home_Add_floating_button_id);
        tvGreeting = findViewById(R.id.activity_home_Greeting_text_view_id);
        tvSubtitle = findViewById(R.id.activity_home_Subtitle_text_view);

        // Connect Calendar TextViews
        tvWeekDay1 = findViewById(R.id.activity_home_WeekDay1_text_view_id);
        tvMonthDay1 = findViewById(R.id.activity_home_MonthDay1_text_view_id);
        tvWeekDay2 = findViewById(R.id.activity_home_WeekDay2_text_view_id);
        tvMonthDay2 = findViewById(R.id.activity_home_MonthDay2_text_view_id);
        tvWeekDay3 = findViewById(R.id.activity_home_WeekDay3_text_view_id);
        tvMonthDay3 = findViewById(R.id.activity_home_MonthDay3_text_view_id);

        // Connect Profile Button
        btnProfile = findViewById(R.id.activity_home_Profile_text_view_id);
    }

    private void setupManagers() {
        // Initialize PlanManager to talk to the API
        planManager = new PlanManager();
    }

    private void setupUI() {
        // Configure the List (RecyclerView) to be a vertical list
        rvDailyMeds.setLayoutManager(new LinearLayoutManager(this));

        // Setup the dates for Yesterday, Today, and Tomorrow
        setupCalendarDates();
    }

    private void setupListeners() {
        // Floating Button -> Open Search Screen
        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, SearchMedicationActivity.class));
        });

        // Profile Text -> Open Profile Screen
        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        });
    }

    private void checkSessionAndLoad() {
        // Get User ID and Name from "Backpack" (SharedPreferences)
        int myUserId = getSharedPreferences("user_session", MODE_PRIVATE).getInt("USER_ID", -1);
        String userName = getSharedPreferences("user_session", MODE_PRIVATE).getString("USER_NAME", "Utilizador");

        if (myUserId != -1) {
            // User is logged in: Show greeting and load plans
            tvGreeting.setText("Olá, " + userName + "!");
            loadPlans(myUserId);
        } else {
            // User not found: Go back to Login
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void loadPlans(int userId) {
        // Ask API for plans
        planManager.getUserPlans(userId, new PlanManager.PlanListener() {
            @Override
            public void onPlansLoaded(List<Plan> plans) {
                // Success: Fill the list with data
                // Note: We pass a "Delete Action" to the adapter here
                rvDailyMeds.setAdapter(new PlanAdapter(plans, plan -> showDeleteConfirmation(plan)));

                // Update subtitle with counts
                if (tvSubtitle != null) {
                    tvSubtitle.setText("Tens " + plans.size() + " medicamentos hoje");
                }
            }

            @Override
            public void onPlanDeleted() {}

            @Override
            public void onError(String message) {
                Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Show a popup to confirm deletion
    private void showDeleteConfirmation(Plan plan) {
        new AlertDialog.Builder(this)
                .setTitle("Apagar Plano")
                .setMessage("Queres deixar de tomar " + plan.med_name + "?")
                .setPositiveButton("Sim, apagar", (dialog, which) -> deletePlan(plan.id))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Call API to delete the plan
    private void deletePlan(int planId) {
        planManager.deletePlan(planId, new PlanManager.PlanListener() {
            @Override
            public void onPlanDeleted() {
                Toast.makeText(HomeActivity.this, "Plano apagado!", Toast.LENGTH_SHORT).show();
                // Reload list to refresh data
                checkSessionAndLoad();
            }

            @Override
            public void onPlansLoaded(List<Plan> plans) {}

            @Override
            public void onError(String message) {
                Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Calculate dates for the top calendar
    private void setupCalendarDates() {
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", new Locale("pt", "PT"));
        SimpleDateFormat numberFormat = new SimpleDateFormat("dd", new Locale("pt", "PT"));
        Calendar calendar = Calendar.getInstance();

        // Go back 1 day (Yesterday)
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (tvWeekDay1 != null) {
            tvWeekDay1.setText(dayFormat.format(calendar.getTime()).toUpperCase().replace(".", ""));
            tvMonthDay1.setText(numberFormat.format(calendar.getTime()));
        }

        // Go forward 1 day (Back to Today)
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        if (tvWeekDay2 != null) {
            tvWeekDay2.setText(dayFormat.format(calendar.getTime()).toUpperCase().replace(".", ""));
            tvMonthDay2.setText(numberFormat.format(calendar.getTime()));
        }

        // Go forward 1 day (Tomorrow)
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        if (tvWeekDay3 != null) {
            tvWeekDay3.setText(dayFormat.format(calendar.getTime()).toUpperCase().replace(".", ""));
            tvMonthDay3.setText(numberFormat.format(calendar.getTime()));
        }
    }
}
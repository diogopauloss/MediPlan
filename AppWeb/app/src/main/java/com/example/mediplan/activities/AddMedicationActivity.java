package com.example.mediplan.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mediplan.R;
import com.example.mediplan.managers.PlanManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import java.util.Calendar;

public class AddMedicationActivity extends AppCompatActivity {

    private TextView tvSelectedMedName;
    private TextInputEditText etEndDate;
    private SwitchMaterial switchChronic;
    private Chip chipBreakfast, chipLunch, chipDinner, chipBedtime;
    private Button btnSaveMed;
    private int selectedMedId = -1;
    private String formattedDate = null;
    private PlanManager planManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        // Link XML IDs to Java variables
        initViews();
        // Initialize the Logic Managers
        setupManagers();
        // Receive data from the previous screen (SearchActivity)
        handleIntentData();
        // Configure Button Clicks and Date Picker
        setupListeners();
    }

    private void initViews() {
        tvSelectedMedName = findViewById(R.id.activity_add_medication_SelectedMedName_text_view_id);
        etEndDate = findViewById(R.id.activity_add_medication_EndDate_text_field_text_id);
        switchChronic = findViewById(R.id.activity_add_medication_Chronic_switch_id);

        // Time selection chips
        chipBreakfast = findViewById(R.id.activity_add_medication_Breakfast_chip_id);
        chipLunch = findViewById(R.id.activity_add_medication_Lunch_chip_id);
        chipDinner = findViewById(R.id.activity_add_medication_Dinner_chip_id);
        chipBedtime = findViewById(R.id.activity_add_medication_Bedtime_chip_id);

        btnSaveMed = findViewById(R.id.activity_add_medication_SaveMed_button_id);
    }

    private void setupManagers() {
        planManager = new PlanManager();
    }

    private void handleIntentData() {
        // Retrieve Medication Name and ID from the Intent ("Backpack")
        String medName = getIntent().getStringExtra("MED_NAME");
        selectedMedId = getIntent().getIntExtra("MED_ID", -1);

        // Show the medication name on screen
        if (medName != null) {
            tvSelectedMedName.setText(medName);
        }
    }

    private void setupListeners() {
        // Open Date Picker when clicking the date field
        etEndDate.setOnClickListener(v -> showDatePicker());

        // Save button click -> Run logic
        btnSaveMed.setOnClickListener(v -> performSavePlan());
    }

    private void performSavePlan() {
        // Validation: Must have an end date OR be chronic
        if (formattedDate == null && !switchChronic.isChecked()) {
            Toast.makeText(this, "Escolhe uma data de fim ou marca 'Crónico'", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get User ID from Session
        int myUserId = getSharedPreferences("user_session", MODE_PRIVATE).getInt("USER_ID", -1);
        if (myUserId == -1) {
            Toast.makeText(this, "Erro: Faz Login novamente.", Toast.LENGTH_LONG).show();
            return;
        }

        // Prepare JSON Data for the API
        JsonObject json = new JsonObject();
        json.addProperty("user_id", myUserId);
        json.addProperty("medication_id", selectedMedId);

        // Check which Chips are selected (true/false)
        json.addProperty("take_breakfast", chipBreakfast.isChecked());
        json.addProperty("take_lunch", chipLunch.isChecked());
        json.addProperty("take_dinner", chipDinner.isChecked());
        json.addProperty("take_bedtime", chipBedtime.isChecked());
        json.addProperty("is_chronic", switchChronic.isChecked());

        // Only add end_date if it's NOT chronic
        if (!switchChronic.isChecked()) {
            json.addProperty("end_date", formattedDate);
        }

        // Send to Manager
        planManager.addPlan(json, new PlanManager.SaveListener() {
            @Override
            public void onSaved() {
                // Success: Go back to Home
                Toast.makeText(AddMedicationActivity.this, "Medicamento adicionado!", Toast.LENGTH_SHORT).show();
                returnToHome();
            }

            @Override
            public void onError(String message) {
                // Error: Show message
                Toast.makeText(AddMedicationActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    // --- HELPER FUNCTIONS (UI) ---

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Show Calendar Popup
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Update Text Field (Visual)
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    etEndDate.setText(selectedDate);

                    // Update Variable for API (Format: YYYY-MM-DD)
                    formattedDate = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                }, year, month, day);
        datePickerDialog.show();
    }

    private void returnToHome() {
        // Go back to Home and clear the "Add Medication" screen from history
        Intent intent = new Intent(AddMedicationActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
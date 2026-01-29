package com.example.mediplan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediplan.R;
import com.example.mediplan.adapters.MedicationAdapter;
import com.example.mediplan.managers.MedicationManager;
import com.example.mediplan.models.Medication;

import java.util.ArrayList;
import java.util.List;

public class SearchMedicationActivity extends AppCompatActivity {

    private RecyclerView rvMedications;
    private MedicationAdapter adapter;
    private MedicationManager medManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_medication);

        // Link XML IDs to Java variables
        initViews();
        // Initialize the Logic Managers
        setupManagers();
        // Configure the List (RecyclerView)
        setupUI();
        // Fetch data from API immediately
        loadAllMedications();
    }

    private void initViews() {
        rvMedications = findViewById(R.id.activity_search_medication_Medications_recycler_view_id);
    }

    private void setupManagers() {
        medManager = new MedicationManager();
    }

    private void setupUI() {
        // Set the list layout to be vertical
        rvMedications.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Adapter with an empty list and a Click Action
        // "this::openAddMedicationScreen" means: When an item is clicked, run that function
        adapter = new MedicationAdapter(new ArrayList<>(), this::openAddMedicationScreen);

        rvMedications.setAdapter(adapter);
    }

    private void loadAllMedications() {
        // We pass "" (empty string) to search for EVERYTHING
        medManager.search("", new MedicationManager.SearchListener() {
            @Override
            public void onFound(List<Medication> medications) {
                // Success: Update the adapter with the new list
                adapter.updateList(medications);
            }

            @Override
            public void onError(String message) {
                // Error: Show message to user
                Toast.makeText(SearchMedicationActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- HELPER FUNCTION: Navigation ---
    private void openAddMedicationScreen(Medication medication) {
        // Prepare to go to the next screen (AddMedicationActivity)
        Intent intent = new Intent(SearchMedicationActivity.this, AddMedicationActivity.class);

        // Pass data to the "Backpack" (Intent) so the next screen knows what was clicked
        intent.putExtra("MED_ID", medication.getId());
        intent.putExtra("MED_NAME", medication.getName());

        startActivity(intent);
    }
}
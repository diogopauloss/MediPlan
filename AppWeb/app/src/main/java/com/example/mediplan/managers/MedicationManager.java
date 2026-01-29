package com.example.mediplan.managers;

import com.example.mediplan.api.RetrofitClient;
import com.example.mediplan.models.Medication;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// This class handles all Medication related API calls (Searching)
public class MedicationManager {

    /* --- Interface to communicate back to the Activity --- */
    public interface SearchListener {
        void onFound(List<Medication> medications); // Success: Returns the list of medicines found
        void onError(String message);               // Error: Returns an error message
    }

    /* --- SEARCH LOGIC --- */
    // The function that performs the magic (Calls the API)
    public void search(String query, SearchListener listener) {

        // Call the API endpoint defined in RetrofitClient
        RetrofitClient.getService().searchMedications(query).enqueue(new Callback<List<Medication>>() {

            @Override
            public void onResponse(Call<List<Medication>> call, Response<List<Medication>> response) {
                // Check if the server response is OK (200) and contains data
                if (response.isSuccessful() && response.body() != null) {
                    // Success: Send the data list back to the Activity
                    listener.onFound(response.body());
                } else {
                    // Server Error: Something went wrong on the API side
                    listener.onError("Erro ao buscar medicamentos");
                }
            }

            @Override
            public void onFailure(Call<List<Medication>> call, Throwable t) {
                // Network Error: No internet or server is down
                listener.onError("Erro de rede: " + t.getMessage());
            }
        });
    }
}
package com.example.mediplan.managers;

import com.example.mediplan.api.RetrofitClient;
import com.example.mediplan.models.Plan;
import com.google.gson.JsonObject;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// This class handles all CRUD operations (Create, Read, Delete) for Plans
public class PlanManager {

    /* --- Interfaces to communicate back to the Activity --- */

    // Used when Loading or Deleting plans
    public interface PlanListener {
        void onPlansLoaded(List<Plan> plans); // Success: Returns list of plans
        void onPlanDeleted();                 // Success: Plan was deleted
        void onError(String message);         // Error: Something went wrong
    }

    // Used specifically when Saving a new plan
    public interface SaveListener {
        void onSaved();                       // Success: Plan saved correctly
        void onError(String message);         // Error: Save failed
    }

    /* --- LOAD PLANS (GET) --- */
    public void getUserPlans(int userId, PlanListener listener) {
        // Ask API for plans belonging to this user
        RetrofitClient.getService().getUserPlans(userId).enqueue(new Callback<List<Plan>>() {
            @Override
            public void onResponse(Call<List<Plan>> call, Response<List<Plan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Success: Send the list to the Activity
                    listener.onPlansLoaded(response.body());
                } else {
                    listener.onError("Erro ao carregar planos");
                }
            }

            @Override
            public void onFailure(Call<List<Plan>> call, Throwable t) {
                listener.onError("Erro de rede: " + t.getMessage());
            }
        });
    }

    /* --- DELETE PLAN (DELETE) --- */
    public void deletePlan(int planId, PlanListener listener) {
        // Ask API to remove a specific plan ID
        RetrofitClient.getService().deletePlan(planId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Success: Notify Activity to refresh the list
                    listener.onPlanDeleted();
                } else {
                    listener.onError("Erro ao apagar plano");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onError("Erro de rede");
            }
        });
    }

    /* --- ADD NEW PLAN (POST) --- */
    public void addPlan(JsonObject json, SaveListener listener) {
        // Send the JSON data to the server
        RetrofitClient.getService().addPlan(json).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    // Success!
                    listener.onSaved();
                } else {
                    // Error Handling: Try to read the real error message from Server
                    try {
                        String erro = response.errorBody() != null ? response.errorBody().string() : "Erro desconhecido";
                        listener.onError("Erro ao gravar: " + erro);
                    } catch (Exception e) {
                        listener.onError("Erro desconhecido ao gravar.");
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                listener.onError("Erro de rede: " + t.getMessage());
            }
        });
    }
}
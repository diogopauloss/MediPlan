package com.example.mediplan.managers;

import android.content.Context;
import android.util.Log;
import com.example.mediplan.api.RetrofitClient;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// This class handles all authentication logic (Login, Register, Delete)
public class AuthManager {

    private Context context;
    /* --- Interfaces to communicate back to the Activity --- */
    // Used for Login and Register
    public interface AuthListener {
        void onSuccess();
        void onError(String message);
    }

    // Used specifically for Deleting Account
    public interface DeleteListener {
        void onSuccess();
        void onError(String message);
    }

    // Constructor: Needs Context to access SharedPreferences (Local Memory)
    public AuthManager(Context context) {
        this.context = context;
    }

    // --- LOGIN LOGIC ---
    public void login(String email, String password, AuthListener listener) {
        // Prepare data in JSON format
        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("password", password);

        // Send Login request to server
        RetrofitClient.getService().login(json).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JsonObject body = response.body();

                        /* --- SAFETY CHECK (Prevents App Crash) --- */
                        // We verify if "user" exists before trying to read it
                        if (body.has("user")) {
                            JsonObject userObj = body.get("user").getAsJsonObject();

                            // Read ID safely (If missing, default to -1)
                            int id = -1;
                            if (userObj.has("id") && !userObj.get("id").isJsonNull()) {
                                id = userObj.get("id").getAsInt();
                            }

                            // Read NAME safely (If missing, default to "Utilizador")
                            String name = "Utilizador";
                            if (userObj.has("name") && !userObj.get("name").isJsonNull()) {
                                name = userObj.get("name").getAsString();
                            }

                            // Save user session in local memory ("Backpack")
                            context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
                                    .edit()
                                    .putInt("USER_ID", id)
                                    .putString("USER_NAME", name)
                                    .apply();

                            // Notify Activity: Success!
                            listener.onSuccess();
                        } else {
                            listener.onError("Erro: Servidor respondeu sem utilizador.");
                        }
                    } catch (Exception e) {
                        // If anything goes wrong during parsing, show error instead of crashing
                        e.printStackTrace();
                        listener.onError("Erro ao ler dados: " + e.getMessage());
                    }
                } else {
                    // Login rejected by server (Wrong email/pass)
                    listener.onError("Login falhou. Email ou password errados.");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Internet error or Server down
                listener.onError("Erro de ligação. Verifica o IP.");
            }
        });
    }

    /* --- REGISTER LOGIC --- */
    public void register(String name, String email, String password, AuthListener listener) {
        // Prepare JSON
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("email", email);
        json.addProperty("password", password);

        // Send Register request
        RetrofitClient.getService().register(json).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess();
                } else {
                    listener.onError("Erro ao criar conta.");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                listener.onError("Erro de ligação.");
            }
        });
    }

    /* --- DELETE USER LOGIC --- */
    public void deleteUser(int userId, DeleteListener listener) {
        // Request server to delete the user account
        RetrofitClient.getService().deleteUser(userId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess();
                } else {
                    listener.onError("Erro ao eliminar.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onError("Erro de rede.");
            }
        });
    }
}
package com.example.mediplan.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8000/";

    // Singleton Instance: We only want ONE Retrofit object for the whole app to save memory
    private static Retrofit retrofit = null;

    /* --- PUBLIC ACCESS METHOD --- */
    // Call RetrofitClient.getService() to get the API ready to use
    public static ApiService getService() {

        // "Lazy Initialization": Create the instance ONLY if it doesn't exist yet
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    // GsonConverter: Automatically converts JSON from server into Java Objects
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        // Create and return the implementation of our API Interface
        return retrofit.create(ApiService.class);
    }
}
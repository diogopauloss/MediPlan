package com.example.mediplan.api;

import com.example.mediplan.models.Medication;
import com.example.mediplan.models.Plan;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/register")
    Call<JsonObject> register(@Body JsonObject body);

    @POST("api/login")
    Call<JsonObject> login(@Body JsonObject body);

    @Headers("Accept: application/json")
    @GET("api/medications/search")
    Call<List<Medication>> searchMedications(@Query("query") String query);

    @GET("api/plans/{user_id}")
    Call<List<Plan>> getUserPlans(@Path("user_id") int userId);

    @Headers("Accept: application/json")
    @POST("api/plans")
    Call<JsonObject> addPlan(@Body JsonObject body);

    @DELETE("api/plans/{id}")
    Call<Void> deletePlan(@Path("id") int id);

    @DELETE("api/users/{id}")
    Call<Void> deleteUser(@Path("id") int id);
}
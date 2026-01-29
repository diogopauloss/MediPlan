<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController; 
use App\Http\Controllers\MedicationController;
use App\Http\Controllers\PlanController; 

// Default Laravel route to get user info (requires token)
Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');

Route::post('/register', [AuthController::class, 'register']);
Route::post('/login', [AuthController::class, 'login']);
Route::get('/medications/search', [MedicationController::class, 'search']);
Route::get('/plans/{user_id}', [PlanController::class, 'getUserPlan']);
Route::post('/plans', [PlanController::class, 'store']);
Route::delete('/plans/{id}', [PlanController::class, 'destroy']);
Route::delete('/users/{id}', [AuthController::class, 'deleteAccount']);
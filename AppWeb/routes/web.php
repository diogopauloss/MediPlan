<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\WebController;

Route::get('/', [WebController::class, 'showLogin'])->name('login');
Route::post('/login', [WebController::class, 'processLogin'])->name('login.process');
Route::post('/logout', [WebController::class, 'logout'])->name('logout');

Route::middleware('auth')->group(function () {
    
    Route::get('/dashboard', [WebController::class, 'dashboard'])->name('dashboard');
    Route::post('/users/{id}/toggle-admin', [WebController::class, 'toggleAdmin'])->name('users.toggle');
    Route::get('/medications', [WebController::class, 'medications'])->name('medications.index');
    Route::post('/medications', [WebController::class, 'storeMedication'])->name('medications.store');
    Route::get('/medications/{id}/edit', [WebController::class, 'editMedication'])->name('medications.edit');
    Route::put('/medications/{id}', [WebController::class, 'updateMedication'])->name('medications.update');
    Route::delete('/medications/{id}', [WebController::class, 'deleteMedication'])->name('medications.delete');
});
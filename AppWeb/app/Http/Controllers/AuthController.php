<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Auth;

// API CONTROLLER: Handles User Authentication for the Android App
// This manages Registration, Login, and Account Deletion
class AuthController extends Controller
{
    /* --- REGISTER (Create new account) --- */
    // Receives Name, Email, and Password from the App
    public function register(Request $request) {
        
        // Validate the incoming data
        // Checks if email is unique, and password has at least 6 charsssssss
        $request->validate([
            'name' => 'required',
            'email' => 'required|email|unique:users',
            'password' => 'required|min:6'
        ]);

        // Create the new User in the database
        $user = User::create([
            'name' => $request->name,
            'email' => $request->email,
            'password' => Hash::make($request->password),
            'id_web' => null 
        ]);

        // Return a success response (JSON) to the App
        return response()->json([
            'message' => 'Conta criada com sucesso!',
            'user' => $user
        ], 201);
    }

    /* --- LOGIN (Enter the app) --- */
    // Receives Email and Password
    public function login(Request $request)
    {
        // Get only the email and password fields
        $credentials = $request->only('email', 'password');

        // Try to log in using Laravel's Auth system
        // It automatically checks if the password matches the hash
        if (Auth::attempt($credentials)) {
            $user = Auth::user();
            
            // Success: Return the user data to the App
            return response()->json([
                'message' => 'Login com sucesso',
                'user' => $user
            ], 200);
        }

        // Failure: Wrong email or password
        return response()->json(['message' => 'Dados incorretos'], 401);
    }

    /* --- 3. DELETE ACCOUNT --- */
    // Removes the user and all their data
    public function deleteAccount($id)
    {
        // Find the user by ID
        $user = \App\Models\User::find($id);

        // Error check: If user doesn't exist
        if (!$user) {
            return response()->json(['message' => 'Utilizador não encontrado'], 404);
        }

        // IMPORTANT: Clean up database!
        // Delete all medical plans associated with this user first
        \App\Models\Plan::where('user_id', $id)->delete();

        // Finally, delete the user account itself
        $user->delete();

        // Send success message
        return response()->json(['message' => 'Conta eliminada com sucesso']);
    }
}
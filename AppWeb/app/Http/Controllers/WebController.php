<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use App\Models\User;
use App\Models\Medication; 

// WEB CONTROLLER: Manages the Admin Website
// Unlike the API controllers, this returns Views (HTML pages), not JSON.
class WebController extends Controller
{
    /* --- AUTHENTICATION (Login / Logout) ---*/

    // Show the Login HTML Page
    public function showLogin() {
        return view('login');
    }

    // Handle the Login Logic
    public function processLogin(Request $request) {
        $credentials = $request->only('email', 'password');

        // Check if email/password matches
        if (Auth::attempt($credentials)) {
            
            // SECURITY CHECK: Is this user an Admin?
            // id_web = 1 (Admin), id_web = 0 or null (App User)
            if (Auth::user()->id_web == 1) {
                // Yes: Go to Dashboard
                return redirect()->route('dashboard');
            } else {
                // No: Log them out immediately and show error
                Auth::logout();
                return back()->withErrors(['email' => 'Não tens permissão para aceder ao site.']);
            }
        }

        // Login failed
        return back()->withErrors(['email' => 'Dados incorretos.']);
    }

    // Log out the user
    public function logout() {
        Auth::logout();
        return redirect()->route('login');
    }

    /* --- 2. USER MANAGEMENT (Dashboard) --- */

    // Main Page: Shows the list of all users
    public function dashboard()
    {
        $users = User::all();
        // Sends the $users list to the 'dashboard' view
        return view('dashboard', compact('users'));
    }

    // Action: Grant or Revoke Admin access
    public function toggleAdmin($id)
    {
        $user = User::find($id);
        
        // Toggle logic: If 1 becomes 0, if 0 becomes 1
        $user->id_web = $user->id_web == 1 ? 0 : 1;
        $user->save();

        return redirect()->back()->with('success', 'Permissões atualizadas!');
    }

    /* --- MEDICATION MANAGEMENT --- */

    // Show the Medications Page (List + Create Form)
    public function medications()
    {
        $medications = Medication::all();
        return view('medications', compact('medications'));
    }

    // Action: Save a new medication to the database
    public function storeMedication(Request $request)
    {
        $request->validate([
            'name' => 'required',
            'dosage' => 'required',
        ]);

        Medication::create($request->all());

        // Redirect back to the list with a success message
        return redirect()->route('medications.index')->with('success', 'Medicamento adicionado!');
    }

    // Page: Show the Edit Form for a specific medication
    public function editMedication($id)
    {
        $medication = Medication::find($id);
        return view('edit_medication', compact('medication'));
    }

    // Action: Update the medication details
    public function updateMedication(Request $request, $id)
    {
        $medication = Medication::find($id);
        $medication->update($request->all());
        return redirect()->route('medications.index')->with('success', 'Medicamento atualizado!');
    }

    // Action: Delete a medication
    public function deleteMedication($id)
    {
        Medication::destroy($id);
        return redirect()->route('medications.index')->with('success', 'Medicamento apagado!');
    }
}
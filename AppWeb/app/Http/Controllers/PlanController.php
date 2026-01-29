<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

// API CONTROLLER: Manages the User's Medication Schedule (The "Plans")
// Handles fetching, creating, and deleting the daily schedules.
class PlanController extends Controller
{
    /* --- GET USER PLANS (List "My Medication") --- */
    public function getUserPlan($user_id)
    {
        // We use the Query Builder to join two tables: 'plans' and 'medications'
        $plans = DB::table('plans')
            ->join('medications', 'plans.medication_id', '=', 'medications.id')
            ->where('plans.user_id', $user_id) // Filter only for this specific user
            ->select(
                'plans.*', // Get all info about the plan (times, dates)
                'medications.name as med_name', // Get the medicine name
                'medications.dosage as med_dosage' // Get the medicine dosage
            )
            ->get();

        // Send the combined data back to Android as JSON
        return response()->json($plans);
    }

    /* --- CREATE NEW PLAN (Add to Schedule) --- */
    // Receives data from the "Add Plan" screen in the App
    public function store(Request $request)
    {
        // Validation: Ensure we know who the user is and what medicine they picked
        $request->validate([
            'user_id' => 'required',
            'medication_id' => 'required',
            'end_date' => 'nullable|date',
        ]);

        // Save to Database
        $plan = \App\Models\Plan::create([
            'user_id' => $request->user_id,
            'medication_id' => $request->medication_id,
            
            // Times of day to take the medicine
            'take_breakfast' => $request->take_breakfast ? 1 : 0,
            'take_lunch' => $request->take_lunch ? 1 : 0,
            'take_dinner' => $request->take_dinner ? 1 : 0,
            'take_bedtime' => $request->take_bedtime ? 1 : 0,
            
            // Is it for life (chronic) or does it have an end date?
            'is_chronic' => $request->is_chronic ? 1 : 0,
            'end_date' => $request->end_date,
        ]);

        // Return success message and the created plan
        return response()->json(['message' => 'Plano criado!', 'plan' => $plan], 201);
    }

    /* --- DELETE PLAN (Remove from Schedule) --- */
    // User swiped or clicked delete in the App
    public function destroy($id)
    {
        // Find the plan by its unique ID
        $plan = \App\Models\Plan::find($id);

        // Error check: Does it exist?
        if (!$plan) {
            return response()->json(['message' => 'Plano não encontrado'], 404);
        }

        // Delete from database
        $plan->delete();

        return response()->json(['message' => 'Plano apagado com sucesso']);
    }   
}
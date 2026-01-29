<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Medication;

class MedicationController extends Controller
{
    public function search(Request $request)
    {
        return response()->json(Medication::all());
    }
}
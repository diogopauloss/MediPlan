<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
public function up(): void
{
    Schema::create('plans', function (Blueprint $table) {

        $table->id();
        $table->foreignId('user_id')->constrained()->onDelete('cascade');
        $table->foreignId('medication_id')->constrained()->onDelete('cascade');
        $table->boolean('take_breakfast')->default(false); 
        $table->boolean('take_lunch')->default(false);     
        $table->boolean('take_dinner')->default(false);    
        $table->boolean('take_bedtime')->default(false);   
        $table->boolean('is_chronic')->default(false);    
        $table->date('end_date')->nullable();          

        $table->timestamps();
    });
}
    public function down(): void
    {
        Schema::dropIfExists('plans');
    }
};

package com.example.mediplan.models;

public class Medication {
    private int id;
    private String name;
    private String dosage;
    private String form;
    private String leaflet;


    public int getId() { return id; }
    public String getName() { return name; }
    public String getDosage() { return dosage; }
    public String getForm() { return form; }
    public String getLeaflet() { return leaflet; }
    @Override
    public String toString() { return name + " " + dosage; }
}
package com.example.mediplan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediplan.R;
import com.example.mediplan.models.Medication;
import java.util.List;

// ADAPTER: This acts as a "Bridge" between the Data (List<Medication>) and the UI (RecyclerView)
public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {

    // The data source
    private List<Medication> medicationList;

    // Listener to handle clicks (Communicates back to the Activity)
    private final OnItemClickListener listener;

    /* --- Interface Definition --- */
    // Defines what happens when an item is clicked
    public interface OnItemClickListener {
        void onItemClick(Medication medication);
    }

    // --- Constructor ---
    public MedicationAdapter(List<Medication> medicationList, OnItemClickListener listener) {
        this.medicationList = medicationList;
        this.listener = listener;
    }

    /* --- Helper Method --- */
    // Call this to update data when search results change
    public void updateList(List<Medication> newList) {
        this.medicationList = newList;
        // Refreshes the UI instantly
        notifyDataSetChanged();
    }

    /* --- 1. CREATE VIEW (Inflate) --- */
    // Creates a new visual block (Card) for an item
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Convert the XML layout into a real Java View
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medication_result, parent, false);
        return new ViewHolder(view);
    }

    /* --- BIND DATA (Fill) --- */
    // Fills the visual block with real data from the list
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the medication at the current position
        Medication med = medicationList.get(position);

        // Set the text
        holder.tvName.setText(med.getName());

        // Format Dosage + Form (e.g., "500mg - Comprimido")
        String form = med.getForm() != null ? med.getForm() : "";
        holder.tvDosage.setText(med.getDosage() + " - " + form);

        // Set Click Listener on the "+" button
        holder.ivAdd.setOnClickListener(v -> listener.onItemClick(med));
    }

    /* --- COUNT ITEMS --- */
    // Tells the RecyclerView how many items to draw
    @Override
    public int getItemCount() {
        return medicationList != null ? medicationList.size() : 0;
    }

    /* --- VIEWHOLDER CLASS --- */
    // Holds references to the visual elements so we don't look them up every time (Performance)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDosage;
        ImageView ivAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Link XML IDs to Java Variables
            tvName = itemView.findViewById(R.id.item_medication_task_MedName_text_view_id);
            tvDosage = itemView.findViewById(R.id.item_medication_result_MedDosage_text_view_id);
            ivAdd = itemView.findViewById(R.id.item_medication_result_Add_image_view_id);
        }
    }
}
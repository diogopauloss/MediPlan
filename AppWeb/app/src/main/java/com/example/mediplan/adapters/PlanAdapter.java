package com.example.mediplan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediplan.R;
import com.example.mediplan.models.Plan;
import java.util.List;

// ADAPTER: Connects the User's Plans (Data) to the RecyclerView (Screen)
public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

    // The data source
    private List<Plan> planList;

    // Listener for Long Clicks (Press and Hold) - Used for Deleting
    private OnPlanLongClickListener longClickListener;

    /* --- Interface Definition --- */
    // Defines the action to take when an item is held down
    public interface OnPlanLongClickListener {
        void onPlanLongClick(Plan plan);
    }

    /* --- Constructor --- */
    public PlanAdapter(List<Plan> planList, OnPlanLongClickListener longClickListener) {
        this.planList = planList;
        this.longClickListener = longClickListener;
    }

    /* --- CREATE VIEW --- */
    // Inflates the XML layout for a single item (Card)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medication_task, parent, false);
        return new ViewHolder(view);
    }

    /* --- BIND DATA --- */
    // Fills the visual block with real data
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the plan at the current position
        Plan plan = planList.get(position);

        // Set simple text fields
        holder.tvMedName.setText(plan.med_name);
        holder.tvDosage.setText(plan.med_dosage);

        /* --- Logic to build the Schedule String --- */
        // Checks which times are active (1 = true) and adds text
        String horarios = "";
        if (plan.take_breakfast == 1) horarios += "Peq.A ";
        if (plan.take_lunch == 1)     horarios += "Almoço ";
        if (plan.take_dinner == 1)    horarios += "Jantar ";
        if (plan.take_bedtime == 1)   horarios += "Deitar";

        holder.tvPeriod.setText(horarios);
        holder.tvTime.setText("Hoje");

        /* --- LONG CLICK MAGIC (For Deletion) --- */
        holder.itemView.setOnLongClickListener(v -> {
            // Trigger the delete action in HomeActivity
            longClickListener.onPlanLongClick(plan);

            // Return 'true' to indicate we handled the click
            // (Prevents the normal click from firing afterwards)
            return true;
        });
    }

    /* --- COUNT ITEMS --- */
    @Override
    public int getItemCount() {
        return planList != null ? planList.size() : 0;
    }

    /* --- VIEWHOLDER CLASS --- */
    // Holds references to the visual elements (Performance)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedName, tvDosage, tvTime, tvPeriod;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Link XML IDs to Java Variables
            tvMedName = itemView.findViewById(R.id.item_medication_task_MedName_text_view_id);
            tvDosage = itemView.findViewById(R.id.item_medication_task_Dosage_text_view_id);
            tvTime = itemView.findViewById(R.id.item_medication_task_Time_text_view_id);
            tvPeriod = itemView.findViewById(R.id.item_medication_task_TimePeriod_text_view_id);
        }
    }
}
package com.arcfit.murasaki;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arcfit.murasaki.model.Plans;

import java.util.ArrayList;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {
    private List<Plans> plans;

    public PlanAdapter(List<Plans> plans) {
        this.plans = plans;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_list, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        Plans plan = plans.get(position);
        holder.planName.setText(plan.exerciseType);
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public void updatePlans(List<Plans> newPlans) {
        this.plans = newPlans;
        notifyDataSetChanged();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView planName;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            planName = itemView.findViewById(R.id.plan_name);
        }
    }
}


package com.arcfit.murasaki;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arcfit.murasaki.model.Plans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {
    private List<Plans> planList = new ArrayList<>();

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise_list, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        Plans plan = planList.get(position);
        holder.planName.setText(plan.exercise_name);

        // Parse and format the deadline date
        String originalDateStr = plan.deadline;
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        originalFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Set the timezone to UTC
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = originalFormat.parse(originalDateStr);
            String formattedDateStr = targetFormat.format(date);
            holder.planDeadline.setText(formattedDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.planDeadline.setText(originalDateStr); // Fallback to original if parsing fails
        }
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public void updatePlans(List<Plans> plans) {
        this.planList = plans;
        notifyDataSetChanged();
    }

    static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView planName;
        TextView planDeadline;

        PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            planName = itemView.findViewById(R.id.plan_name);
            planDeadline = itemView.findViewById(R.id.plan_deadline);
        }
    }
}


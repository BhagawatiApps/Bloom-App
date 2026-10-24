package com.example.bloomapp.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloomapp.Data.Entity.DailyProgressEntity;
import com.example.bloomapp.Data.Entity.HabitEntity;
import com.example.bloomapp.R;
import com.example.bloomapp.Utils.HabitColorHelper;
import com.example.bloomapp.Utils.HabitManager;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.List;

public class DailyProgressAdapter extends RecyclerView.Adapter<DailyProgressAdapter.ViewHolder> {

    public interface OnHabitClickListener {
        void onHabitClick(HabitEntity habit);
    }

    private final Context context;
    private List<HabitEntity> habitList;
    private final OnHabitClickListener listener;

    public DailyProgressAdapter(Context context, List<HabitEntity> habitList, OnHabitClickListener listener) {
        this.context = context;
        this.habitList = habitList;
        this.listener = listener;
    }

    public void updateList(List<HabitEntity> newList) {
        this.habitList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_d_progress, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HabitEntity habit = habitList.get(position);

        String fullTitle = habit.title;
        String mainTitle = fullTitle;
        if (fullTitle != null && fullTitle.contains("·")) {
            mainTitle = fullTitle.split("·")[0].trim();
        }

        holder.tvTitle.setText(mainTitle);

        DailyProgressEntity progress = HabitManager.getHabitProgressForToday(context, habit.habitId);
        int currentVal = (progress != null) ? progress.currentValue : 0;
        boolean isCompleted = (progress != null && progress.isCompleted);

        String unitStr = habit.unit != null ? habit.unit : "";
        holder.tvSubtitle.setText((isCompleted ? habit.targetValue : currentVal) + "/" + habit.targetValue + " " + unitStr);

        holder.progressCircular.setMax(Math.max(1, habit.targetValue));
        holder.progressCircular.setProgress(isCompleted ? habit.targetValue : currentVal);

        HabitColorHelper.HabitColor habitColor = HabitColorHelper.getHabitColor(context, habit.habitId);
        holder.progressCircular.setIndicatorColor(habitColor.primaryColor);
        holder.progressCircular.setTrackColor(habitColor.trackColor);
        holder.ivIcon.setImageTintList(ColorStateList.valueOf(habitColor.primaryColor));

        loadHabitIcon(holder.ivIcon, habit.iconRes);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onHabitClick(habit);
            }
        });
    }

    private void loadHabitIcon(ImageView imageView, String logoStr) {
        if (logoStr != null && !logoStr.trim().isEmpty()) {
            try {
                int resId = Integer.parseInt(logoStr);
                Glide.with(context)
                        .load(resId)
                        .placeholder(R.drawable.ic_meals)
                        .error(R.drawable.ic_meals)
                        .into(imageView);
                return;
            } catch (NumberFormatException ignored) {
            }

            int resId = context.getResources().getIdentifier(logoStr, "drawable", context.getPackageName());
            if (resId != 0) {
                Glide.with(context)
                        .load(resId)
                        .placeholder(R.drawable.ic_meals)
                        .error(R.drawable.ic_meals)
                        .into(imageView);
                return;
            }

            Glide.with(context)
                    .load(logoStr)
                    .placeholder(R.drawable.ic_meals)
                    .error(R.drawable.ic_meals)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_meals);
        }
    }

    @Override
    public int getItemCount() {
        return habitList != null ? habitList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircularProgressIndicator progressCircular;
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvSubtitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            progressCircular = itemView.findViewById(R.id.progressCircular);
            ivIcon = itemView.findViewById(R.id.ivProgressIcon);
            tvTitle = itemView.findViewById(R.id.tvProgressTitle);
            tvSubtitle = itemView.findViewById(R.id.tvProgressSubtitle);
        }
    }
}

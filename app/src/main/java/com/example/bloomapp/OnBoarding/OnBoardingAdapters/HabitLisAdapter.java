package com.example.bloomapp.OnBoarding.OnBoardingAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloomapp.OnBoarding.OnBoardingModels.HabitListModel;
import com.example.bloomapp.R;
import com.example.bloomapp.Utils.HabitManager;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.ArrayList;

public class HabitLisAdapter extends RecyclerView.Adapter<HabitLisAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<HabitListModel> habitListModel;
    private OnHabitStateChangeListener listener;

    public interface OnHabitStateChangeListener {
        void onHabitStateChanged(HabitListModel model, int position, boolean isChecked);
    }

    public HabitLisAdapter(Context context, ArrayList<HabitListModel> habitListModel) {
        this.context = context;
        this.habitListModel = habitListModel;
    }

    public void setOnHabitStateChangeListener(OnHabitStateChangeListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_habit, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HabitListModel model = habitListModel.get(position);

        // Load Icon
        loadHabitIcon(holder.ivHabitIcon, model.getHabitLogo());

        // Set Text
        holder.tvHabitTitle.setText(model.getHabitTitle());
        holder.tvHabitDescription.setText(model.getHabitDescription());

        // Manage Habit State from Room/Model
        boolean isChecked = model.getHabitState() != null && model.getHabitState();

        // Clear previous listener to prevent triggering while recycling
        holder.switchHabit.setOnCheckedChangeListener(null);
        holder.switchHabit.setChecked(isChecked);

        // Handle Switch toggle
        holder.switchHabit.setOnCheckedChangeListener((buttonView, checked) -> {
            int adapterPos = holder.getBindingAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION) {
                HabitListModel currentItem = habitListModel.get(adapterPos);
                currentItem.setHabitState(checked);
                HabitManager.updateHabitState(context, currentItem.getHabitId(), checked);

                if (listener != null) {
                    listener.onHabitStateChanged(currentItem, adapterPos, checked);
                }
            }
        });

        // Allow toggling switch by tapping the entire row
        holder.itemView.setOnClickListener(v -> holder.switchHabit.toggle());
    }

    private void loadHabitIcon(ImageView imageView, String logoStr) {
        if (logoStr != null && !logoStr.trim().isEmpty()) {
            try {
                // Parse integer resource ID (e.g. String.valueOf(R.drawable.ic_meals))
                int resId = Integer.parseInt(logoStr);
                Glide.with(context)
                        .load(resId)
                        .placeholder(R.drawable.ic_meals)
                        .error(R.drawable.ic_meals)
                        .into(imageView);
                return;
            } catch (NumberFormatException ignored) {
            }

            // Check if drawable resource name (e.g. "ic_meals")
            int resId = context.getResources().getIdentifier(logoStr, "drawable", context.getPackageName());
            if (resId != 0) {
                Glide.with(context)
                        .load(resId)
                        .placeholder(R.drawable.ic_meals)
                        .error(R.drawable.ic_meals)
                        .into(imageView);
                return;
            }

            // Fallback: load as URL or File Path
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
        return habitListModel != null ? habitListModel.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHabitIcon;
        TextView tvHabitTitle, tvHabitDescription;
        MaterialSwitch switchHabit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHabitIcon = itemView.findViewById(R.id.ivHabitIcon);
            tvHabitTitle = itemView.findViewById(R.id.tvHabitTitle);
            tvHabitDescription = itemView.findViewById(R.id.tvHabitDescription);
            switchHabit = itemView.findViewById(R.id.switchHabit);
        }
    }
}

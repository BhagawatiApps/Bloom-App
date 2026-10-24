package com.example.bloomapp.Utils;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import com.example.bloomapp.R;

public class HabitColorHelper {

    public static class HabitColor {
        @ColorInt
        public final int primaryColor;

        @ColorInt
        public final int trackColor;

        public HabitColor(@ColorInt int primaryColor, @ColorInt int trackColor) {
            this.primaryColor = primaryColor;
            this.trackColor = trackColor;
        }
    }

    public static HabitColor getHabitColor(Context context, String habitId) {
        if (habitId == null) {
            return getDefaultColor(context);
        }

        String id = habitId.toLowerCase();
        if (id.contains("meal")) {
            return new HabitColor(
                    ContextCompat.getColor(context, R.color.habit_meal_primary),
                    ContextCompat.getColor(context, R.color.habit_meal_track)
            );
        } else if (id.contains("movement")) {
            return new HabitColor(
                    ContextCompat.getColor(context, R.color.habit_movement_primary),
                    ContextCompat.getColor(context, R.color.habit_movement_track)
            );
        } else if (id.contains("water")) {
            return new HabitColor(
                    ContextCompat.getColor(context, R.color.habit_water_primary),
                    ContextCompat.getColor(context, R.color.habit_water_track)
            );
        } else if (id.contains("relaxation")) {
            return new HabitColor(
                    ContextCompat.getColor(context, R.color.habit_relaxation_primary),
                    ContextCompat.getColor(context, R.color.habit_relaxation_track)
            );
        } else if (id.contains("sleep")) {
            return new HabitColor(
                    ContextCompat.getColor(context, R.color.habit_sleep_primary),
                    ContextCompat.getColor(context, R.color.habit_sleep_track)
            );
        } else if (id.contains("cycle")) {
            return new HabitColor(
                    ContextCompat.getColor(context, R.color.habit_cycle_primary),
                    ContextCompat.getColor(context, R.color.habit_cycle_track)
            );
        } else if (id.contains("symptom")) {
            return new HabitColor(
                    ContextCompat.getColor(context, R.color.habit_symptom_primary),
                    ContextCompat.getColor(context, R.color.habit_symptom_track)
            );
        }

        return getDefaultColor(context);
    }

    private static HabitColor getDefaultColor(Context context) {
        return new HabitColor(
                ContextCompat.getColor(context, R.color.habit_meal_primary),
                ContextCompat.getColor(context, R.color.habit_meal_track)
        );
    }
}

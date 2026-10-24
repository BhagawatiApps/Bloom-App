package com.example.bloomapp.Utils;

import android.content.Context;

import com.example.bloomapp.Data.AppDatabase;
import com.example.bloomapp.Data.Entity.DailyProgressEntity;
import com.example.bloomapp.Data.Entity.HabitEntity;
import com.example.bloomapp.OnBoarding.OnBoardingModels.HabitListModel;
import com.example.bloomapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HabitManager {

    public static void initializeDefaultsIfNeeded(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        if (db.habitDao().getHabitCount() == 0) {
            String[] ids = {"meal", "movement", "water", "relaxation", "sleep", "cycle", "symptom"};
            String[] titles = {
                    "Meals · 3 balanced meals",
                    "Movement · 30 minutes",
                    "Water · 8 glasses",
                    "Relaxation · 15 minutes",
                    "Sleep · 8 hours",
                    "Cycle · period dates",
                    "Symptom check-in"
            };
            String[] descriptions = {
                    "Steadier blood sugar helps keep PCOS symptoms in check.",
                    "Supports insulin sensitivity, a key lever in PCOS.",
                    "Helps with energy and bloating.",
                    "Lower stress means lower cortisol, which can worsen symptoms.",
                    "Consistent sleep helps balance hormones over time.",
                    "Spot your personal pattern and predict your next period.",
                    "Track how you feel day to day to notice patterns over time."
            };
            int[] logos = {
                    R.drawable.ic_meals,
                    R.drawable.ic_movement,
                    R.drawable.ic_water,
                    R.drawable.ic_relaxation,
                    R.drawable.ic_sleep,
                    R.drawable.ic_cycle,
                    R.drawable.ic_symptom
            };
            int[] targets = {3, 30, 8, 15, 8, 1, 1};
            String[] units = {"meals", "minutes", "glasses", "minutes", "hours", "days", ""};

            List<HabitEntity> defaultList = new ArrayList<>();
            for (int i = 0; i < titles.length; i++) {
                defaultList.add(new HabitEntity(ids[i], titles[i], descriptions[i], String.valueOf(logos[i]), targets[i], units[i], false));
            }
            db.habitDao().insertAll(defaultList);
        }
    }

    public static ArrayList<HabitListModel> getDefaultHabits(Context context) {
        initializeDefaultsIfNeeded(context);
        AppDatabase db = AppDatabase.getInstance(context);
        List<HabitEntity> entities = db.habitDao().getAllHabits();
        ArrayList<HabitListModel> habitList = new ArrayList<>();

        for (HabitEntity entity : entities) {
            HabitListModel model = new HabitListModel(entity.habitId, entity.title, entity.description, entity.iconRes, entity.enabled);
            habitList.add(model);
        }

        return habitList;
    }

    public static ArrayList<HabitListModel> getUntrackedHabits(Context context) {
        initializeDefaultsIfNeeded(context);
        AppDatabase db = AppDatabase.getInstance(context);
        List<HabitEntity> entities = db.habitDao().getUntrackedHabits();
        ArrayList<HabitListModel> habitList = new ArrayList<>();

        for (HabitEntity entity : entities) {
            HabitListModel model = new HabitListModel(entity.habitId, entity.title, entity.description, entity.iconRes, entity.enabled);
            habitList.add(model);
        }

        return habitList;
    }

    public static List<HabitEntity> getEnabledHabits(Context context) {
        initializeDefaultsIfNeeded(context);
        AppDatabase db = AppDatabase.getInstance(context);
        return db.habitDao().getEnabledHabits();
    }

    public static void updateHabitState(Context context, String habitId, boolean isEnabled) {
        AppDatabase db = AppDatabase.getInstance(context);
        db.habitDao().updateHabitEnabled(habitId, isEnabled);
    }

    public static void updateHabitCompletion(Context context, String habitId, boolean isCompleted) {
        AppDatabase db = AppDatabase.getInstance(context);
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        HabitEntity habit = db.habitDao().getAllHabits().stream().filter(h -> h.habitId.equals(habitId)).findFirst().orElse(null);
        int target = habit != null ? habit.targetValue : 1;

        DailyProgressEntity progress = new DailyProgressEntity(habitId, today, isCompleted ? target : 0, isCompleted);
        db.dailyProgressDao().insertOrUpdateProgress(progress);
    }

    public static DailyProgressEntity getHabitProgressForToday(Context context, String habitId) {
        initializeDefaultsIfNeeded(context);
        AppDatabase db = AppDatabase.getInstance(context);
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return db.dailyProgressDao().getProgress(habitId, today);
    }

    public static boolean isHabitCompletedToday(Context context, String habitId) {
        DailyProgressEntity progress = getHabitProgressForToday(context, habitId);
        return progress != null && progress.isCompleted;
    }

    public static int getCompletedHabitsCount(Context context) {
        initializeDefaultsIfNeeded(context);
        AppDatabase db = AppDatabase.getInstance(context);
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return db.dailyProgressDao().getCompletedCountForDate(today);
    }


    public static int getEnableHabitsCount(Context context) {
        initializeDefaultsIfNeeded(context);
        AppDatabase db = AppDatabase.getInstance(context);
        return db.habitDao().getEnabledHabitCount();
    }


    public static int getTotalHabitsCount(Context context) {
        initializeDefaultsIfNeeded(context);
        AppDatabase db = AppDatabase.getInstance(context);
        int enabledCount = db.habitDao().getEnabledHabitCount();
        return enabledCount > 0 ? enabledCount : 5;
    }
}

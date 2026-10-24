package com.example.bloomapp.Data.Entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "daily_progress", primaryKeys = {"habitId", "date"})
public class DailyProgressEntity {
    @NonNull
    public String habitId;
    @NonNull
    public String date; // "yyyy-MM-dd"
    public int currentValue;
    public boolean isCompleted;

    public DailyProgressEntity(@NonNull String habitId, @NonNull String date, int currentValue, boolean isCompleted) {
        this.habitId = habitId;
        this.date = date;
        this.currentValue = currentValue;
        this.isCompleted = isCompleted;
    }
}

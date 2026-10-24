package com.example.bloomapp.Data.Entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "habits")
public class HabitEntity {
    @PrimaryKey
    @NonNull
    public String habitId;
    public String title;
    public String description;
    public String iconRes;
    public int targetValue;
    public String unit;
    public boolean enabled;

    public HabitEntity(@NonNull String habitId, String title, String description, String iconRes, int targetValue, String unit, boolean enabled) {
        this.habitId = habitId;
        this.title = title;
        this.description = description;
        this.iconRes = iconRes;
        this.targetValue = targetValue;
        this.unit = unit;
        this.enabled = enabled;
    }
}

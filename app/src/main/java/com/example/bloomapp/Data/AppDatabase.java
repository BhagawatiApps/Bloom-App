package com.example.bloomapp.Data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bloomapp.Data.Dao.DailyProgressDao;
import com.example.bloomapp.Data.Dao.HabitDao;
import com.example.bloomapp.Data.Entity.DailyProgressEntity;
import com.example.bloomapp.Data.Entity.HabitEntity;

@Database(entities = {HabitEntity.class, DailyProgressEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract HabitDao habitDao();
    public abstract DailyProgressDao dailyProgressDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "bloom_habit_db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

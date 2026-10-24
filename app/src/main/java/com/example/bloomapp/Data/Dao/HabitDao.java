package com.example.bloomapp.Data.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bloomapp.Data.Entity.HabitEntity;

import java.util.List;

@Dao
public interface HabitDao {
    @Query("SELECT * FROM habits")
    List<HabitEntity> getAllHabits();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHabit(HabitEntity habit);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<HabitEntity> habits);

    @Update
    void updateHabit(HabitEntity habit);

    @Query("UPDATE habits SET enabled = :enabled WHERE habitId = :habitId")
    void updateHabitEnabled(String habitId, boolean enabled);

    @Query("SELECT COUNT(*) FROM habits")
    int getHabitCount();

    @Query("SELECT COUNT(*) FROM habits WHERE enabled = true")
    int getEnabledHabitCount();

    @Query("SELECT * FROM habits WHERE enabled = false")
    List<HabitEntity> getUntrackedHabits();

    @Query("SELECT * FROM habits WHERE enabled = true")
    List<HabitEntity> getEnabledHabits();
}

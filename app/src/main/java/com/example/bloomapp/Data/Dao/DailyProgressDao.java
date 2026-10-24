package com.example.bloomapp.Data.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bloomapp.Data.Entity.DailyProgressEntity;

import java.util.List;

@Dao
public interface DailyProgressDao {
    @Query("SELECT * FROM daily_progress WHERE date = :date")
    List<DailyProgressEntity> getProgressForDate(String date);

    @Query("SELECT * FROM daily_progress WHERE habitId = :habitId AND date = :date")
    DailyProgressEntity getProgress(String habitId, String date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateProgress(DailyProgressEntity progress);

    @Query("SELECT COUNT(*) FROM daily_progress WHERE date = :date AND isCompleted = 1")
    int getCompletedCountForDate(String date);
}

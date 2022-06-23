package de.rub.selab22a15.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MoodDao {
    @Query("SELECT * FROM mood")
    LiveData<List<Mood>> getMoodData();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Mood mood);

    @Query("DELETE FROM mood")
    void delete();
}
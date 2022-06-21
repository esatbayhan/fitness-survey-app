package de.rub.selab22a15.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EmotionDao {
    @Query("SELECT * FROM emotion")
    LiveData<List<Emotion>> getEmotionData();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Emotion emotion);

    @Query("DELETE FROM emotion")
    void delete();
}
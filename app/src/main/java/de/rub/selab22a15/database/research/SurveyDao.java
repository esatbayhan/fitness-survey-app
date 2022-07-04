package de.rub.selab22a15.database.research;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SurveyDao {
    @Query("SELECT * FROM survey")
    LiveData<List<Survey>> getSurveyData();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Survey survey);

    @Query("DELETE FROM survey")
    void delete();
}

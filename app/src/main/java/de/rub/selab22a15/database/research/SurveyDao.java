package de.rub.selab22a15.database.research;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SurveyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Survey survey);

    @Query("SELECT * FROM survey")
    List<Survey> getAll();

    @Query("DELETE FROM survey")
    void delete();
}
